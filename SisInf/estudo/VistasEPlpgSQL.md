# PlpgSQL

### Declaração de variáveis

```sql
DECLARE
    name [CONSTANT] type [COLLATE collation_name] [NOT NULL] [ { DEFAULT | := } expression ];

--ex.
DECLARE
    x INT DEFAULT 30;
    url VARCHAR := 'http://www.google.com';
    valor CONSTANT INT := 10;
```

### Afetação de variáveis

```sql
variable { := | = } expression;

SELECT expression INTO name FROM 
{INSERT | UPDATE | DELETE} ... RETURNING expression INTO name;
```

### Condição

```sql
IF condition THEN
    statements;
ELSEIF
    statements;
ELSE
    statements;
END IF;
```

### Ciclo

**loop:**

```sql
LOOP
    statements;
END LOOP;
```
**ex:**

```sql
do $$
declare x int=0;
begin
    loop
        x = x + 10;
        if x in (20, 50) then
            continue;
        end if;
        raise notice 'x =%', x;
        exit when x > 60;
    end loop;
end $$ language plpgsql;
```

**while:**

```sql
WHILE condition LOOP
    statements;
END LOOP;
```

**ex:**

```sql
do
$$
declare x int=-5;
begin
    WHILE x <= 20 LOOP
        x = x+5;
        IF x = 10 then
        continue;
        end if;
        raise notice 'x = %', x;EXIT when x = 15;
    END LOOP;
end $$ language plpgsql;
```

**for:**

```sql
FOR name IN [ REVERSE ] expression .. expression [BY expression] LOOP
    statements;
END LOOP;
```

**ex:**

```sql
do
$$
declare x int;
begin
    FOR x IN REVERSE 20..0 BY 5 LOOP
        IF x = 10 then
            continue;
        end if;
        raise notice 'x = %', x;
        EXIT when x = 5;
    END LOOP;
end $$ language plpgsql;
```

**for (variante com cursores):**

```sql
FOR recordvar IN bound_cursorvar [([ argument_name := ] argument_value [, ...])] LOOP
    statements;
END LOOP;
```

**ex:**

```sql
do language plpgsql $$
declare
    r int;
    c cursor for select j from t;
begin
    OPEN c;
    fetch next from c into x;
    while found loop
        raise notice 'valor de j=%', x; 
        fetch next from c into x;
    end loop;
    close c;
end $$;
```

**"found" é uma variável pré-definida. com FETCH é `true` se o FETCH suceder e `false` caso contrário**

### Exceções

```sql
RAISE EXCEPTION 'message' [USING option = value [, ...]];
```

ex:

```sql
exception
    when sqlstate '50001' then
        get stacket diagnostion t1 = MESSAGE_TEXT,
                                t2 = RETURNED_SQLSTATE;

        raise notice 'second block:';
        raise notice 'MESSAGE_TEXT: %', t1;
        raise notice 'SQLSTATE: %', t2;
END;
```

## Procedures

### Porquê?

- Maior desempenho 
- Bom encapsulamento
- Maiores níveis de reutilização
- Maior segurança
  
- **Em geral**, não se deve usar os procedimentos armazenados para a implementação da lógica aplicacional.
- A sua utilização deve ser restringida a aspetos que tenham a ver com a natureza intríseca dos dados, como validação de regras de negócio.

```sql
CREATE PROCEDURE nome_procedimento (param [type])
LANGUAGE plpgsql
AS $$
BEGIN
    statements;
END;

call nome_procedimento(param);
```

- deve-se separar o controlo transacional da lógica aplicacional em dois procedimentos como ilustrado a seguir:

```sql
create or replace procedure t2_logica(p1 int, p2 int)
language plpgsql
as $$
begin
    insert into t values(p1, p1);
    if p1 = p2 then
        RAISE EXCEPTION 'Chave duplicada %', 
                                p1 using errcode '50001';
    end if;
    insert into t values(p2, p2);
end $$;

create or replace procedure t2_trans(p1 int, p2 int)
language plpgsql
as $$
begin
    call t2_logica(p1, p2);
    --commit / Este commit não se deve colocar porque uma transaçãoi não deve ser acabada num bloco com controlo de exceções porém caso este ponto seja atingido haverá um commit implícito
    exception
    when sqlstate '450000' then 
        -- mais processamento
        rollback;
    -- mais exceções
    when other then 
        -- mais processamento 
        rollback;
end $$;

create or replace procedure t2_NivIsol(p1 int, p2 int)
language plpgsql
as $$
begin
    rollback; 
    set transaction isolation level repeatable read;
    call t2_trans(p1, p2);
end $$;
```



### TODO

- estudar cursores
- estudar JPA
