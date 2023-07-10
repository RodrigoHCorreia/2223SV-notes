# Exame EN 2021-22

# Ex 1

a)

**Função para obter next tarefa:**
```sql
create function nextTarefa() returns int
language plpgsql
as
$$
declare
    i int;
begin
    select max(id) into i from tarefa;
    if i is null then
        return 0;
    else
        return i+1;
    end if;
end; $$
```

**Função para inicializar funcionarios sem tarefa**
```sql
create procedure inicializarFuncionarioSemTarefas_logica()
language plpgsql
as
$$
declare
    i int;
begin
    if not exists (select id from tarefa where nome='Sem tarefa') THEN 
        insert into tarefa values (nextTarefa(), 'Sem tarefa')
    end if;

    select id into i from tarefa where nome='Sem tarefa';
    insert into funcionario_tarefa select num, i as id_tarefa from funcionario where not exists (select num_func from funcionario_tarefa where num_func=num);
end; $$
```

**Transacional:**
```sql
create procedure inicializarFuncionarioSemTarefas_trans()
language plpgsql
as
$$
begin
    --set transaction isolation level repeatable read;
    call inicializarFuncionarioSemTarefas_logica();
    exception when others then
        rollback;
        raise exception 'Erro: %', sqlerrm;
end; $$
```

> Sempre que possível tentar usar inserts diretamente em vez de ciclos e cursores.


b)

**Função que devolve todos os funcionarios:**

```sql
create function getFuncionarios(linf int) returns table(num int, nome varchar, num_tarefas int)
language plpgsql
as
$$
begin
    return query selec num, nome, (select count(*) from funcionario_tarefa where num_func=num and id_tarefa not in (select id from tarefa where tarefa.nome='Sem tarefa')) from tabela where idade<=linf; 
end;$$
```

```sql
declare
    n int;
    x record;

for x in select num, nome from funcionario where idade<=linf loop
    select count(*) into n from funcionario_tarefa where num_func=x.num and id_tarefa not in (select id from tarefa where tarefa.nome='Sem tarefa');
```

```java
RepositorioFun(){
    Func create(fun f){
        try{
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("exame");
            EntityManager em = emf.createEntityManager();

            Tarefa t;
            Query q = createQuery("select t from Tarefa t where t.nome='Sem tarefa'");
            t = q.getSingleResult();
            qf.setTarefa(t);
            f.persist();
            em.commit();
        }
        finally{
            emf.close();
            em.close();
        }
    }
}
```

2.

a)

escalonamento possível:
2.1, 2.2, 2.3, 1.1, 1.2, 1.2, ...

possível é 
1) ... 2.4, 2.5, 2.6, 1.4, 1.5

2) ... 1.4, 1.5, 2.4, 2.5, 2.6

diz-se um escalonamento não ser possível se fica a aguardar por um lock.

b)

1) ... 2.4, 2.5, 2.6, 1.4, 1.5

read commited:
2.3 -> (3, 3)
2.4 -> (2,2)
2.5 -> (1,1)

repeatable read:
2.3 -> (3, 3)
2.4 -> (2,2)
2.5 -> (1,1)

2) ... 1.4, 1.5, 2.4, 2.5, 2.6

read commited:
2.3 -> (3, 3)
2.4 -> (2,22)
2.5 -> (1,11)

repeatable read:
2.3 -> (3, 3)
2.4 -> (2,2)
2.5 -> (1,1)
