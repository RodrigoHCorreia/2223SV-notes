# Aula 18

TODO: rever a aula de dia 15/05

Como diminuir o número de threads porque estas são honorosas
queremos pelo menos tantas threads quanto CPUs

as corrotinas garantem que no ambito de uma corrotina existe hb entre ações antes de um ponto de suspensão com ações depois do ponto de suspensão

- Confinar - deixar de ter partilha entre threads
- no trabalho apenas 1 thread pode aceder a um dos canais unidirecionais do socket


## Work related stuff 

---
**Exemplo dado no git:** (com threads)
por cada cliente: (ConnectedClient)

1 thread principal por cliente : que está bloqueada na fila para fazer coisas em nome do cliente.

1 thread que está bloqueada à leitura.

Room - quando recebe uma mensagem e quer enviar para todos os clientes, mete em todas as listas de mensagens dos ConnectedClients.
O servidor também pode escrever na lista de mensagem para todos os connectedClients para avisar que vão entrar em shutdown

não temos espera múltipla, quando fazemos um read do socket a thread fica bloqueada até receber uma mensagem do socket.
não fazemos polling, usamos espera passiva por 1 coisa.


-----
**Trabalho a fazer** (com corrotinas)

2 corrotinas por cliente na mesma, uma para ler e outra para escrever.
já não temos garantia que ocorre na mesma thread, mas não temos problemas de multi-threading pelo HB garantido pelas corrotinas.

implementar a fila (ex1)

Vamos usar outro tipo de socket implementado por nós (ex2)
NIO2(new IO) - possui assynchronous socket channels, não bloqueante, mas é mais complicado de usar. porem não estão relacionados com as corrotinas, logo temos de fazer essa relação. (falado na próxima semana).

## Corroutines

- **runBlocking** - função **NÃO SUSPEND**, recebe um bloco suspend recetora de um CoroutineScope, cria uma corrotina e arranca uma corrotina defenida pela função lambda, fica à espera que a corrotina acabe, este cria ainda internamente um CoroutineScope.
- **launch** - recebe um CoroutineScope garantindo assim que a corrotina criada 
- **GlobalScope** - é um CoroutineScope que não tem pai, logo não temos garantia que a corrotina acabe, logo não devemos usar na maior parte dos casos.
- **coroutineScope** - função **suspend**, recebe um bloco suspend recetora de um CoroutineScope, cria um CoroutineScope e arranca uma corrotina defenida pela função lambda, só retorna quando todas as corrotinas criadas no seu ambito terminarem.

## Concorrência estruturada

- **Active** - o seu código ainda está a executar
- **Completing** - o seu código já acabou mas as corrotinas filhas não terminaram
- **Completed** - tudo o que foi criada por ela já terminou
- **Cancelling** - a corrotina está a ser cancelada
- **Cancelled** - a corrotina já foi cancelada