# Aula 14 - cursors, 

## PLPGSQL 

- for x in select * from where
- for x in cursor loop
- ...
- end loop

- PLPGSQL é uma linguagem procedural que permite a criação de funções, triggers, procedures, etc.
- começa com do $$ e termina com $$
- contador com resto 3, conta 0-1-2-0-1-2...

- **Banco de dados:**
```sql
drop table if exists tx;
create table tx(i int primary key, j varchar(10));
insert into tx values(1, 'aa'), (11,'bb'), (111, 'cc'), (1111, 'dd'), (11111, 'ee');
```

- **Exemplo sem cursor:**
```sql
do -- language plpgsql
$$
declare r record;
    cont int = 0;

begin
    for r in select i from tx loop
        if cont = 0 then
            delete from tx where i = r.i;
        end if;
        cont = (cont + 1) % 3; -- contador com modulo 3
    end loop;
end;
$$;

select * from tx;
```

- **Exemplo com cursor:**
```sql
do -- language plpgsql
$$
declare r record;
    cont int = 0;
    curs cursor for select i from tx;
    
begin
    for r in curs loop
        if cont = 0 then
            delete from tx where current of curs;
        end if;
        cont = (cont + 1) % 3; -- contador com modulo 3
    end loop;
end;
$$;

select * from tx;
```

- **Exemplo com cursor e fetch:**
```sql

do -- language plpgsql
$$
declare vi int;
    cont int = 0;
    curs scroll cursor for select i from tx;
    
begin
    open curs;
    fetch first from curs into i;
    while found loop
        delete from tx where current of curs;
    fetch relative +3 from curs into vi;
    end loop;
end;
$$;

select * from tx;
```

## Exceções ou condition handling

