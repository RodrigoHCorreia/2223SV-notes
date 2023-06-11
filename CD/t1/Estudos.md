# Estudos para o teste

## Sistemas de Comunicação Digital 



## Formulas e coisas para formulário

- **Tb** - tempo de bit
- **Rb** - ritmo binário = 1/Tb
- **BER** - Bit Error Rate = #bits errados / #bits transmitidos
- **Terr** - tempo médio esperado entre erros consecutivos = Tb/BER 
- **Tx** - duração de transmissão de um conteúdo binário com #bits = #bits*Tb

- **OOK - On-Off Keying** -> 1 bit por sinal (se tiver amplitude 1, é 1, se tiver amplitude 0, é 0)
- **FSK - Frequency Shift Keying** -> 1 bit por sinal (se tiver frequência elevada por tempo de sinal, é 1, se tiver frequência 0 por tempo de sinal, é 0)
- **PSK - Phase Shift Keying** -> 1 bit por sinal (quando começa o sinal, se o sinal estiver a 0, é 0, se estiver a 1, é 1, quando há troca de fase, é troca de 0 para 1 e vice-versa)

- **Ponto a ponto** - 1 transmissor e 1 receptor
- **Ponto a multi-ponto** - 1 transmissor e vários receptores
- **Multi-ponto a multi-ponto** - vários transmissores e vários receptores

- **Cabo UTP** - Unshielded Twisted Pair (telefone)
- **Cabo STP** - Shielded Twisted Pair
- **Cabo coaxial** - Possui largura de manda maior que os pares traçados, melhor desempenho (tv por cabo e maior distância de transmissão)
- **Fibra ótica** - Melhor desempenho, maior largura de banda, maior distância de transmissão (redes de alta velocidade)
    - Singlemode - 1 caminho de luz
    - Multimodo - multiplos caminhos de luz
    - Multimodo gradual - multiplos caminhos de luz, porém de forma gradual

- **Transmissão série** - 1 bit por vez, usado em curta, média e longa distância	
- **Transmissão paralela** - vários bits por vez, usado em curta distância

- **Transmissão sincrona** - mecanismo de sincronização com relógio presente no dispositivo receptor
- **Transmissão assincrona** - não é usado nenhum mecanismo de sincronização, pois as sequências de bits são marcadas com um Start bit e um Stop bit
- **CAN** - **two wire, half duplex, high-speed** network 
  - usa ou TP ou **bit-stuffing**
  - insere um bit invertido a cada 5 bits iguais para manter a sincronização
  - as mensagens têm prioridade.

## Duvidas

- banda base ou banda canal diferenças? - Banda base é ou bit a 1 ou bit a 0, banda canal é com sinosoides.
 