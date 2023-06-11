# Cancelamento de corrotinas

o suspendCoroutine não é sensível a cancelamento.

Soluções:

- usar `suspendCancellableCoroutine`.
- `suspendCancellableCoroutine` chama automaticamente a continuação em caso de cancelamento.
- Faz o EQUIVALENTE ao completeWithException.
- Está preparada para "race" entre chamada explicita ao cancel e a chamada automática pelo suspendCancellableCoroutine.

**Uma coroutina filha com erro, cancela a corrotina pai, A MENOS que seja uma CancellationException (ou derivada).**

porém é possível cancelar o comportamento descrito acima, usando o supervisorScope.
garantindo que a corrotina pai não é cancelada.

