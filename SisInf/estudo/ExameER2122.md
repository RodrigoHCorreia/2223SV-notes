# Exame ER 2021_22

a)

**duvida**

```sql
CREATE OR REPLACE VIEW Veiculo AS
SELECT
    vlId as id,
    vlMatricula as matricula,
    vlCilindrada as cilindrada,
    vlLugares as lugares,
    null as tara,
    vlCondutor as condutor,
    'ligeiro' as tipo,
FROM veiculoLigeiro
UNION ALL
SELECT
    vlId as id,
    vlMatricula as matricula
    vlCilindrada as cilindrada
    null as lugares
    vlTara as tara
    vlCondutor as condutor
    'pesado' as tipo
FROM veiculoPesado;
```

b)

```sql
CREATE OR REPLACE PROCEDURE InsereVeiculo(matricula VARCHAR, cilindada INT, lugares INT, tara INT, condutor INT, tipo VARCHAR)
language plpgSQL
as
$$
begin
    if exists(select * from veiculoLigeiro vl join veiculoPesado vp on vl.vlCondutor = vp.vlCondutor where vlCondutor = condutor) then
        raise exception 'driver % already busy', condutor;
    end if;

    if tipo='ligeiro' then
        if exists(select * from veiculoPesado where vlMatricula=matricula) then
            raise exception 'matricula % is already in the veiculoPesado table', matricula;
        end if;
        insert into veiculoLigeiro(vlMatricula, vlCilindrada, vlLugares, vlCondutor) values (matricula, cilindrada, lugares, condutor)
    elseif tipo='pesado' then
        if exists(select * from veiculoLigeiro where vlMatricula=matricula) then
            raise exception 'matricula % is already in the veiculoLigeiro table', matricula;
        end if;
        insert into veiculoPesado(vlMatricula, vlCilindrada, vlLugares, vlCondutor) values (matricula, cilindrada, tara, condutor)
    else 
        raise exception 'invalid type %', tipo;
    endif;
end;
$$;
```

**dúvida:** Admita que existe o condutor indicado e que ele não está associado a nenhum veículo. (Isto significa que não podemos fazer chamada à função da b?)
**duvida:** como era suposto Justificar. 

c)

```sql
CREATE OR REPLACE TRIGGER inserirVeiculoTrigger
INSTEAD OF INSERT ON Veiculo
FOR EACH ROW execute function inserirVeiculo();

create or replace function inserirVeiculo() returns trigger 
language plpgsql
as
$$
begin
    call InsereVeiculo(new.matricula, new.cilindrada, new.lugares::integer, new.tara::integer, new.condutor, new.tipo);
    return NEW;
end;
$$;

```

d)

```sql
CREATE OR REPLACE PROCEDURE InsereVeiculo(matricula VARCHAR, cilindada INT, lugares INT, tara INT, condutor INT, tipo VARCHAR)
language plpgSQL
as
$$
begin
    if exists(select * from veiculoLigeiro vl join veiculoPesado vp on vl.vlCondutor = vp.vlCondutor where vlCondutor = condutor) then
        raise exception 'driver % already busy', condutor;
    end if;

    if tipo='ligeiro' then
        if exists(select * from veiculoPesado where vlMatricula=matricula) then
            raise exception 'matricula % is already in the veiculoPesado table', matricula;
        end if;
        insert into veiculoLigeiro(vlMatricula, vlCilindrada, vlLugares, vlCondutor) values (matricula, cilindrada, lugares, condutor)
    elseif tipo='pesado' then
        if exists(select * from veiculoLigeiro where vlMatricula=matricula) then
            raise exception 'matricula % is already in the veiculoLigeiro table', matricula;
        end if;
        insert into veiculoPesado(vlMatricula, vlCilindrada, vlLugares, vlCondutor) values (matricula, cilindrada, tara, condutor)
    else 
        raise exception 'invalid type %', tipo;
    endif;
end;
$$;
```

d)

```sql
CREATE or REPLACE PROCEDURE InsereVeiculoTrans(matricula VARCHAR, cilindada INT, lugares INT, tara INT, condutor INT, tipo VARCHAR)
language plpgsql
as
$$
begin
    rollback;
    set transaction isolation level repeatable read;
    begin
        call InsereVeiculo(matricula, cilindrada, lugares, tara, condutor, tipo)
        exception
            when others then
                rollback;
                raise exception 'Erro: %', sqlerrm;
    end;
end;
$$;
```

2. 
a)

**dúvida**: podemos meter //Getters e setters ou temos de os meter por escrito

```java
@Table(name="condutor")
@Entity
public class Condutor{

    public condutores() {}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer cId;

    private String cNome;

    
    public Condutor(String nome){
        this.cNome = nome;
    }

    //Getters e Setters
}

@Table(name="veiculoLigeiro")
@Entity
public class VeiculoLigeiro {
    @Id
    @GenerateValue(strategy=GenerationType.IDENTITY)
    private Integer vlId;

    @Column(nullable=false)
    private String vlMatricula;

    @Column(nullable=false)
    private Integer vlCilindrada;

    @Column(nullable=false)
    private Integer vlLugares;

    @OneToOne(mappedBy="cNome", nullable=false)
    private Condutor vlCondutor;

    public VeiculoLigeiro(){}

    public VeiculoLigeiro(String matricula, Integer cilindrada, Integer lugares, Condutor condutor) {
        this.vlMatricula = matricula;
        this.vlCilindrada = cilindrada;
        this.vlLugares = lugares;
        this.vlCondutor = condutor;
    }

    public VeiculoLigeiro(String matricula, Integer cilindrada,  Condutor condutor) {
        VeiculoLigeiro(matricula, cilindrada, 5, condutor);
    }

    //Getters e Setters
    
}
```

```java
public void inserirVeicLigCondutor(EntityManager em, VeicLigCondDTO vlc) throws Exception {
    // Verificar se o condutor já existe
    Condutor condutorExistente = em.find(Condutor.class, vlc.nomeCond);
    if (condutorExistente != null) {
        throw new Exception("Condutor com o nome '" + vlc.nomeCond + "' já existe.");
    }

    // Verificar se o veículo ligeiro já existe
    VeiculoLigeiro veiculoLigeiroExistente = em.find(VeiculoLigeiro.class, vlc.matricula);
    if (veiculoLigeiroExistente != null) {
        throw new Exception("Veiculo ligeiro com a matricula '" + vlc.matricula + "' já existe.");
    }

    // Proceder com a criação e inserção do condutor e do veículo ligeiro
    Condutor condutor = new Condutor();
    condutor.setNome(vlc.nomeCond);

    VeiculoLigeiro veiculoLigeiro = new VeiculoLigeiro();
    veiculoLigeiro.setMatricula(vlc.matricula);
    veiculoLigeiro.setCilindrada(vlc.cilindrada);
    veiculoLigeiro.setLugares(vlc.lugares);
    veiculoLigeiro.setCondutor(condutor);

    em.persist(condutor);
    em.persist(veiculoLigeiro);
}
```

```java
import javax.persistence.*;

public class BusinessLogic {

    public void inserirVeicLigCondutor(VeicLigCondDTO vlc) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-ER");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Condutor condutor = new Condutor();
            condutor.setNome(vlc.nomeCond);

            VeiculoLigeiro veiculoLigeiro = new VeiculoLigeiro();
            veiculoLigeiro.setMatricula(vlc.matricula);
            veiculoLigeiro.setCilindrada(vlc.cilindrada);
            veiculoLigeiro.setLugares(vlc.lugares);
            veiculoLigeiro.setCondutor(condutor);

            em.persist(condutor);
            em.persist(veiculoLigeiro);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
```
