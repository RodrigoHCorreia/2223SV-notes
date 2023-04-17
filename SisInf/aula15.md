# Aula 15

## Exceções em PL/pgSQL

- **Exemplo:**
```sql

do -- language plpgsql
$$
declare x record;
begin
    select * into strict x from t where i > 10;
    exception
        when no_data_found then
            raise notice 'Nao existem linhas';
        when too_many_rows then
            raise notice 'Encontradas mais do que 1 linha';

end; $$ language plpgsql;
```

SQLSTATE por código (organizados por classes) ou nome de condição

Error Code | Condition name
---|---
Class 00 — Successful Completion
00000 | successful_completion
Class 01 — Warning
01000 | warning
... | ...
01P01 | deprecated_feature
Class 02 — No Data (this is also a warning class per the SQL standard)
02000 | no_data
... | ...
Class XX — Internal Error
XX000 | internal_error
... | ...

- **Exemplo:**
```sql

do
$$
declare nrows int;
begin
    delete from t where i = 2;
    IF NOT FOUND THEN
        raise notice 'não foram apagadas linhas';
    end if;
end; $$ language plpgsql;
```

no_data_found pode ser substituído por SQLSTATE 'P0002'

**level** pode ser:
    - debug5, debug4, debug3, debug2, debug1, log, notice, warning, error.

## Procedimentos armazenados 

##### Vantagens:
- **Maior desempenho:**
  - Dados processados no local onde estão armazenados permitem tirar partido das características do SGBD; 
  - Diminuição do tráfego de dados entre cliente e servidor
  - Mas, se exagerarmos, também podemos sobrecarregar o SGBD com processamentos não muito fortemente ligados aos dados.
- **Bom encapsulamento:**
  - A utilização de procedimentos evita que se acedam diretamente aos dados, contribuindo para uma maior consistência dos mesmos
- **Maiores níveis de reutilização:**
  - Procedimentos podem ser partilhados por várias aplicações
- **Maior segurança:**
  - Utilizadores podem ter permissão para executar um procedimento e não para aceder diretamente aos dados; lógica ocultada para os clientes

- Em geral, não se devem usar os procedimentos armazenados para a implementação da lógica aplicational.
- A sua utilização deve ser restringida a aspetos que tenham a ver com a natureza intríseca dos dados, por exemplo, validação de regras de negócio.

- **Exemplo **PLPGSQL**

```sql
create table tab (id int primary key, valor real);

-- Pretende-se construir o procedimento armazenado mediaNemN que recebe dois parâmetros: no primeiro é indicado um passo (n) e no segundo é devolvido o valor da média das colunas de nome valor da tabela tab, calculada considerando apenas os elementos de n em n.
-- Exemplo: mediaNemN(2, media) devolve a média das colunas valor de tab, considerando apenas os elementos de 2 em 2.

create or replace procedure mediaNemN(n int, out media real)
as
$$
declare
    v real;
    cont int = 0;
    soma real = 0;
    curs cursor for select valor from tab;
begin
    open curs;
    fetch first from curs into v;
    while found loop
        soma = soma + v;
        cont = cont + 1;
    fetch relative + n from curs into v;
    end loop;
    if (cont = 0) then
        media = 0;
    else
        media = soma / cont;
    end if;
end; $$ language plpgsql;

do
$$
declare
    media real;
    
begin
call mediaNemN(3, media);
    raise notice 'media = %', media;
end; $$ language plpgsql;


select * from tab;

```