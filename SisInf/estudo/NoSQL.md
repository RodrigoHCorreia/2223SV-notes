# NoSQL

- Outros modelos aumentam a escalabilidade e aumenta a flexibilidade nos domínios.

## Teorema CAP

- **Consistency:** Todos os nós veem a mesma imagem;
- **Availability:** Todos os nós que não estão em falha devem dar uma resposta razoável dentro de um tempo razoável;
- **Partition Tolerance:** O sistema suporta falhas nas comunicações entre nós.

## Teorema BASE

- **Basically Available** - Em caso de falhas de comunicação, optar pela disponibilidade;
- **Soft state:** - O estado do sistema pode alterar-se mesmo sem novos inputs (devido a replicação assíncrona);
- **Eventual consistency** - O sistema evolui para um estado consistente se não se fornecerem novos inputs durante um tempo adequado.

## Modelos NoSQL

**Orientadas a agregados:**
- Pares chave-valor 
- Famílias de colunas
- Orientadas a documentos
**Não orientadas a agregados:**
- Orientadas a grafos

**Vantagens de usar agregados:**
- Escalabilidade através de sharding, cada agregado fica localizado no mesmo nó;
- Em acessos pela chave, evitam-se JOINs;
- Cada agregado funciona como uma unidade atómica, não obrigando a transações.
- Cada agregado pode ser replicado de forma independente.

![tipos-de-dados](image-1.png)

