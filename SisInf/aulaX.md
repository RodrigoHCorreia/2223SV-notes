# Aula - Exercícios sobre gatilhos

**Testes intrusivos** - alteram a base de dados para testar, porém como ainda não está em fase de produção, não há problema.

**Testes não intrusivos** - não alteram a base de dados, mas têm de simular o comportamento da base de dados.

## Exercícios 


### Exercício proposto 4 gatilhos


Decidir se o gatilho é de nivel **statement** ou **registo**.

- Se for nível registo trata-se 1 a 1 
    - Before - quando o gatilho corre a tabela ainda não tem as linhas que foram inseridas, porém como é para cada registo, a média ao tratar o segundo registo já tem o primeiro registo, logo a  média é influenciada por esse registo.
    - After - quando o gatilho corre a tabela já tem as linhas que foram inseridas, logo a média é influenciada por essas linhas.

- se for statement trata-se todos de uma vez
  - Before - quando o gatilho corre a tabela ainda não tem as linhas que foram inseridas, logo a média não é influenciada por essas linhas.
  - After - quando o gatilho corre a tabela já tem as linhas que foram inseridas, logo a média é influenciada por essas linhas.

- Neste exercício o correto seria utilizar um gatilho de nível **statement** com o gatilho **before**.
- Porém temos de usar um gatilho after pelo motivo abaixo:
```sql
Create trigger tj before insert on contas -- Só podemos fazer isto com gatilhos after, logo isto não se pode fazer.
referencing newtable as novo

```

Deveria-se então fazer o exercício da seguinte forma:

```sql
Create trigger tj after insert on contas
referencing newtable as novo
for each statement
execute procedure f_tj();

Create or replace function f_tj() returns trigger 
language plpgsql
as $$
begin
    select avg(saldo) into nr from contas;
    where numConta not in (select numConta from novo);
    insert into Ricos select numConta from novo where saldo > nr;
    return null;
end;
$$
```

### Exercício proposto 8 gatilhos

- não é correto fazer select * from T where...

Passos:
  - criar uma tabela chamada viatura nova (matricula, ano, preco, cilindrada)
  - depois copiar todos os dados da tabela viatura para a tabela viatura nova
  - colocar cilindrada a 0 em todos os registos da tabela viatura nova
  - alterar e meter a restrição que a coluna não pode ser null
  - apagar a tabela viatura e criar uma vista.
  - defenir gatilhos sobre insert, update e delete na vista

Resolução:
```sql	

create procedure mover()
language plpgsql
as $$
begin
    create table viaturaNova
        (matricula char(8) primary key, ano int, preco float);
    LOCK TABLE viatura IN SHARE mode;
    insert into viaturaNova select * from viatura;
    alter table viaturaNova add column cilindrada int;
    update viaturaNova set cilindrada = 0;
    alter table viaturaNova
        add constraint cc check (cilindrada is not null);
    drop table viatura;
    create view viatura as 
        select matricula, ano, preco from viaturaNova;

create or replace function f_ex8() returns trigger
language plpgsql
as $f$ -- $f$ é o delimitador
begin
    insert into viaturaNova
        values(new.matricula, new.ano, new.preco, 0);
    return new;
end;
$f$;

create trigger tViatura
INSTEAD OF insert on viatura
for each row execute function f_ex8();
...
commit;
end;
$$;

```

- **Uma aplicação bem feita deve usar o mínimo de select * from T where ... exatamente por causa do exercício acima.**


#### A estudar:

[]Triggers

[]Procedures

[]Functions

[]Referencing

[]Exceptions

[]Raise notice

[]Modos(SHARE, UPDATE, ETC.)