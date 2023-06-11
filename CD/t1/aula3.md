# Aula 3 - Exemplos de aplicações; Características do SCD; Deteção e correção de erros; Meios de transmissão

## Indicadores chaves S.C.D.

- **Tb** - tempo de bit, em segundos
- **Rb = 1/Tb** - ritmo binário, em bits por segundo 
- **BER = #bits errados/#bits transmitidos** - Bit Error Rate, taxa de erro de bit (tipicamente 10^-6)
- **Terr** - tempo médio esperado entre erros consecutivos, em segundos
- **Tx = #bits * Tb** - duração de transmissão de um conteúdo binário com #bits, em segundos
- **1 kilobyte** = 1kB = 1000 byte
- **1 kbibyte** = 1kiB = 1024 byte

## Exemplos de aplicações de SCD

* ADSL
* Redes de computadores
  * LAN
  * WAN
* Wi-Fi
* WiMax
* IrDA - Infrared Data Association (comunicação por infravermelhos, PAN - Personal Area Network)
* Bluetooth
* Comunicação Movéis
* NFC, USB
* Televisão Digital 
* Câmeras IP

## Características do SCD

1. Tipo de Ligação
2. Direção da transmissão
3. Tipo de sinal transmitido
4. Banda de frequência

<img text="Diagrama" alt="Diagrama" src="../CD/docs/aula3/diagrama.png">

<img text="Diagrama2" alt="Diagrama2" src="../CD/docs/aula3/diagrama2.png">

### Tipo de ligação

- Ponto-a-ponto
- Ponto a multi-ponto 
- multi-ponto a multi-ponto

### Direção da transmissão

- _Simplex_ - transmissão em uma única direção; o recetor não responde pelo mesmo canal
- _Duplex_ - transmissão nos dois sentidos no mesmo canal; os dois equipamentos possuem emissor e recetor
  - _Half-Duplex_ - a largura de banda é totalmente ocupada por um sentido de cada vez; é usada uma direção de cada vez
  - _Full-Duplex_ - a largura de banda é repartida de forma a que a comunicação seja possível em simultâneo nos dois sentidos

<img src="../CD/docs/aula3/direcao.png">

- _Simplex_ - Televisão, rádio...
- _Half-Duplex_ - Walkie-talkie, ligação entre computadores...
- _Full-Duplex_ - Telefone, ligação entre computadores...

### Tipo de sinal transmitido

- _Análogo_ - Envio de sinal analógico, o qual deve ser reproduzido no recetor
  - Exemplo: sinal áudio produzido por um microfone, rádio, sinal de vídeo (analógico) produzido por uma câmara...
- _Digital_ - sinal discreto - envio de uma sequência de bits
  - Exemplo: sinal de vídeo (digital) produzido por uma câmara, sinal de áudio digital produzido por um microfone, sinal de dados produzido por um computador...

### Banda de frequência

- Banda base (código de linha)
  - Uso de códigos de linha (ondas "quadradas") colocados diretamente no meio de transmissão, sem transação na frequência
  - O meio de transmissão admite componentes de frequência em torno de 0 Hz
  - Os meios de transmissão são cabos (ou fibra); **não é possível transmitir wireless**
- Banda canal (modulações digitais)
  - Uso de sinusóides para posicionar o espetro em determinadas frequência; este processo é designado por **modulação**
  - Aproveitamento eficiente da largura de banda do meio de transmissão
  - Escolha da frequência de transmissão e das bandas.


## Meios de transmissão

1. Cabos metálicos (Cabos trançados, coaxial)
2. Fibra ótica
3. "Ar"

### Pares Trançados UDP e STP

- **Unshielded Twisted Pair (UTP)** - pares trançados não protegidos
- **Shielded Twisted Pair (STP)** - pares trançados protegidos

<img src="../CD/docs/aula3/cabos.png">

### Cabo coaxial

- Largura de banda superior.
- Atenuação menor.

<img src="../CD/docs/aula3/coaxial.png">

<img src="../CD/docs/aula3/coaxial2.png">

### Fibra ótica

- Transmissão de dados em forma de luz

<img src="../CD/docs/aula3/fibra.png">
<img src="../CD/docs/aula3/fibra2.png">

### TPC Exercícios dos slides