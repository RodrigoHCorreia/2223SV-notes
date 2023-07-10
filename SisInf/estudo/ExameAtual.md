1) Numa plataforma que permite gerir funcionários e respetivos chefes foram criadas as seguintes tabelas:

```sql
create table Funcionarios(
 num int primary key,
 nome varchar(255) not null,
 idade int not null,
 tipo char check (tipo in (‘C’,’N’)),
 -- 'C' - chefe, 'N' não chefe
 chefe int references FuncChefes
 check (chefe <> num)
);

create table FuncChefes(
 num int primary key
 references Funcionarios,

 telefone char(9) not null
);
```
a) Quando se tentou criar as duas tabelas, por qualquer ordem obteve-se um erro. Justifique porque
esse erro ocorreu e escreva o código que o permitiria eliminar. 

R: O erro acontece porque existe uma referência circular entre as duas tabelas. 

```sql
begin transaction;
    create table Funcionarios(
        ...
    );

    create table FuncChefes(
        num int primary key,
        telefone char(9) not null
    );

    alter table FuncChefes add constraint c foreign key (chefe) references Funcionario;

```

b) Crie a função numChefiados, sem parâmetros, que devolve uma tabela de pares (nome do
funcionário, nº de chefiados) para todos os chefes existentes na base de dados. 

```sql
create or replace function numChefiados() 
returns table (nomeCh varchar(255), numSub int)
language plpgsql
as 
$$
begin
    return query select nome as nomeCh,
                        (select count(*) from funcionarios where chefe = f.num) as numSub
                 from funcionarios f where 
                      tipo = 'C';
end;
$$;
```

c)
```sql
create or replace procedure despromoverLog(chAct int)
language plpgsql
as $$
declare x integer;
begin
    if not exists(select * from funcChefes where num=chAct) then
        RAISE EXCEPTION sqlstate '45001' USING MESSAGE 'Não é chefe';
    end if;
    for x in select num from funcionarios where chefe = chAct loop
        update funcionarios set chefe = menosChefiados(chAct) where num = x;
    end loop;
    delete from funcChefes where num=chAct;
    update funcionarios set tipo = 'N' where num = chAct;
end;
$$;

create or replace procedure despromover(chAct int)
language plpgsql
as $$
begin
    rollback;
    begin
        call despromoverLog(chAct);
        exception
            when others then
                rollback;
                raise exception 'Erro: %', sqlerrm;
    end;
end;
$$;
```

d) Crie a vista funcChefesV que devolve informação de num, nome, idade e telefone do chefe de
todos os funcionários que não são chefes, sem apresentar informação duplicada. (Nota: no
enunciado original a vista era designada funcChefes, mas esse nome já existe associado a uma
tabela).

```sql
create or replace view funcChefes
as
    select f.num,f.nome,f.idade,c.telefone
    from funcionarios f natural join funcChefes c
    where exists (select * from funcionarios where chefe = c.num and tipo = 'N');
```

e) Apresente o código de um mecanismo que garanta que se uma instrução UPDATE sobre a tabela
Funcionarios alterar a coluna chefe para lhe definir um chefe (ou alterar o antigo), de forma
automática o novo chefe seja inserido na tabela FuncChefes se lá não existir. Nesse caso o n.º de
telefone do novo chefe deverá ser ‘000000000’. O novo chefe deve já existir na tabela
Funcionarios

```sql
create or function fte() returns trigger
language plpgsql
as 
$$
begin
    if new.Chefe is not null and 
        not exists(select * from funcChefes where num = new.Chefe) then
        insert into funcChefes values (new.chefe, '000000000');
        update funcionarios set tipo='C' where num = new.Chefe;
    end if;
return new;
end;
$$;

create or replace trigger tge before update of chefe
on funcionarios
FOR EACH ROW 
    execute function fte();
```

2) Considere a base de dados com as tabelas do exercício 1.

a) Apresente o código das classes de modelo (entidades) para modelar as tabelas apresentadas
anteriormente de modo a serem utilizadas em JPA. Inclua todas as associações de forma
bidirecional. Use anotações JPA. Para evitar ter de construir todos os getters e setters, pode
usar propriedades públicas. 

```java
@Entity
@Table(name="funcionarios")
public class Funcionario {
    @Id
    public Integer num;
    public Integer idade;
    public String nome;
    public String tipo;

    @OneToOne(mappedBy="funcionario")
    public FuncChefe funcC;

    @ManyToOne
    @JoinColumn(name="chefe")
    public FuncChefe chefe;

    public Funcionario() { }

}

@Entity
@Table(name="funcchefes")
public class FuncChefes {
    @Id
    public Integer num;
    public String telefone;

    @OneToOne
    @JoinColumn(name="num")
    @MapsId("num")
    public Funcionario funcionario;

    @OneToMany(mappedBy="chefe")
    public List<Funcionario> chefiados;

    public FuncChefes() { }

}
```

b) Usando JPA, implemente o método criar do repositório FuncionarioMapper que, garantindo
gestão transacional, insira o funcionário passado como parâmetro, tendo em conta se ele é do
tipo “C” (Chefe) ou não “N”. Se for do tipo “C” deve inserir o respetivo registo na tabela
FuncChefes, com o número de telefone “000000000. Deve usar as entidades definidas em (a). 

```java
public class FuncionarioMapper {

    public Integer criar(Funcionario f) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("xpto");
        EntityManager em = emf.createEntityManager();

        try {
                em.getTransaction().begin();
                if(f.getTipo().equals("C")){
                    FuncChefe fc = new FuncChefe();
                    fc.setChefiados(null);
                    fc.setFuncionario(f);
                    fc.setTelefone("000000000");
                    f.setFuncC(fc);
                }
                em.persist(f);
                em.getTransaction().commit();
                return f.getNum();
        } catch (Exception e) {
            if(e.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
```

c) Considere a classe FuncDTO com membros públicos nome (String) e numero (int). Usando
JPA, implemente o método List<FuncDTO> obterEquipa(int numFunc) da classe
FuncionarioRep que retorna a lista composta por instâncias de FuncDTO correspondentes
ao funcionário com número numFunc e funcionários chefiados por ele se ele for chefe, ou gera
uma exceção se não for. O primeiro elemento da lista será a instância de FuncDTO
correspondente ao funcionário numFunc e os restantes as instâncias de FuncDTO para os
funcionários que são chefiados por ele. Deve usar as entidades definidas em (a). 

```java
public class FuncDTO {
    public int numero;
    public String nome;

    public FuncDTO(int num, String nome) {
        this.numero = num;
        this.nome = nome;
    }
}

public class FuncionarioRep {

    public List<FuncDTO> obterEquipa(int numFunc) throws Exception {7
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("xpto");
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Funcionario f = em.find(Funcionario.class, numFund);
            if(f == null)
                throw new Exception("Funcionário "+numFunc+ " não existe");
            if(!f.getTipo().equals("C"))
                throw new Exception("Funcionário "+numFunc+ " não é chefe");
            List<FuncDTO> l = new ArrayList<FuncDTO>();
            l.add(new FuncDTO(f.getNum(), f.getNome()));
            for(Funcionario x: f.getFuncC().getChefiados()) {
                l.add(new FuncDTO(x.getNum(), x.getNome()));
            }

            em.getTransaction().commit();
            return l;
        } catch(Exception e) {
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
```

d) Usando JPA diretamente (isto é, sem outras classes como mappers, repositórios, etc.),
implemente o método transferirChefiados(int numF1, int numF2) da classe BusinessLogic
que, numa transação, transfere todos os chefiados do funcionário com número numF1 para o
funcionário com número numF2. Se algum dos funcionários não for chefe, deve ser gerada
uma exceção. Deve usar as entidades definidas em (a)

```java

public void transferirChefiados(int numF1, int numF2) throws Exception {
     EntityManagerFactory emf = Persistence.createEntityManagerFactory("xpto");
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Funcionario f1 = em.find(Funcionario.class, numF1);
            if(f1==null)
                throw new Exception("Funcionario "+numF1+" inexistente");
            Funcionario f2 = em.find(Funcionario.class, numF2);
            if(f1==null)
                throw new Exception("Funcionario "+numF2+" inexistente");
            if(!f1.getTipo().equals("C"))
                throw new Exception("Funcionario "+numF1+" não chefe");
            if(!f2.getTipo().equals("C"))
                throw new Exception("Funcionario "+numF2+" não chefe");
            
            List<Funcionario> l1 = f1.getFuncC().getChefiados();
            if(l1 != null){
                if(f2.getFuncC().getChefiados() == null)
                    f2.getFuncC().setChefiados(new ArrayList<Funcionario>());
                for(Funcionario x: l1)
                    f2.getFuncC().addChefiado(x);
                f1.getFuncC().setChefiados(null);
            }
            em.getTransaction().commit();
        } catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally{
            em.close();
            emf.close();
        }
}
```


3) Considere a tabela seguinte criada em postgresql, com os registos indicados bem como as duas
transações indicadas, executadas após a criação e preenchimento da tabela seguinte com os valores
indicados:

```sql
create table t1(a int primary key, b int);
insert into t1 values(1,1),(2,2),(3,3);
create table t2(a int primary key, b int);
insert into t2 values(4,4),(5,5),(6,6);
```

Considere também as seguintes instruções:

```sql
(1) update t1 set b = 11 where a = 1;
(2) update t2 set b = 55 where a = 5;
(3) select * from t1 where a = 2;
(4) select * from t2 where a = 6 FOR UPDATE;
(5) select * from t1 where a = 1 FOR SHARE;
(6) update t2 set b = -b;
(7) select * from t2;
```
Considere ainda duas transações com o padrão indicado a seguir:

```sql
(1.1) start transaction; -- T1
(1.2) set transaction isolation level ?
(1.3) ?
(1.4) ?
(1.5) ?
(1.6) commit


(2.1) start transaction; -- T2
(2.2) set transaction isolation level ?
(2.3) ?
(2.4) ?
(2.5) commit;
```

Sabendo que na mesma transação não pode repetir instruções:

a) Indique quais os níveis de isolamento e quais as instruções da lista acima que poderiam ser
colocadas nos locais assinalados com ? para poderem existir escalonamentos de T1 e T2 não
recuperáveis. Se não existir tal possibilidade, justifique-o; caso contrário, apresente um
escalonamento onde isso acontece. 

R: Num escalonamento não recuperável tem de haver uma transação que faça um dirty read e que
termine com commit antes da que escreveu o dirty read termine. Dado que a base de dados
utilizada é postgresql, nunca existem dirty reads e, por consequência, não existem
escalonamentos não recuperáveis. 


