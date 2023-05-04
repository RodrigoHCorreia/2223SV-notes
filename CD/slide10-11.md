# Código de controlo de erros - Codificação de canal

## Algoritmos

Sem código de controlo de erros o BER = p 
com as tecnicas de codificação de canal o BER entre o ponto de entrada e o ponto de saída do canal é menor que p, e ser 0 idealmente.

Codificador de canal (n, k) - n bits de saída e k bits de entrada.
(8,7) - por cada 8 bits introduzimos 1 bit redundante.

Descodificador de Canal - FEC (Forward Error Correction) e ARQ (Automatic Repeat Request)

FEC - trabalha em qualquer modo de transmissão
ARQ - trabalha em modo de transmissão duplex


1) Código de reflexão
   1) (3, 1) - 1 bit de entrada e 3 bits de saída
   2) (5, 1) - 1 bit de entrada e 5 bits de saída
   3) No código de reflexão o n é sempre impar
   4) Usa ARQ
2) Código de bit de paridade 
   1) ultimo bit é o XOR dos restantes
   2) (3, 2)
   3) (4, 3)
   4) Usa ARQ
3) Código de hamming
   1) 
   2) (7, 4) - 4 bits de entrada e 7 bits de saída
   3) (15, 11) - 11 bits de entrada e 15 bits de saída
   4) Fazer vários bits de paridade.
   5) Usa ARQ ou FEC
4) Código Cícliclo de Redundância (CRC)
   1) Divisão de polinómios para calcular os bits redundantes
   2) Resto da divisão da mensagem pelo polinómio gerador
   3) CRC32 - 32 bits redundantes, fazer a divisão por um polinómio de grau 32.
   4) Funciona normalmente em modo ARQ (é possível fazer em FEC mas obriga a ter a distância mínima maior que 3)
   5) Qualquer rotação da palavra código é uma palavra código

### Propriedades dos códigos

1) Ritmo de código R = k/n = k/(k+q) (q #bits redundantes)
2) Distância de Hamming dh (ci, cj) = #bits diferentes entre ci e cj dh([1 1 0], [0 1 1]) = 2
3) Distância mínima (dmin): é a menor distância de Hamming entre duas quaisquer palavras código; depende da redundância.
4) Capacidade de deteção de **até** 'l' bits em erro l = dmin - 1
5) Capacidade de correção de **até** 't' bits em erro t = (dmin - 1)/2 FLOOR, arredonda para baixo

Distância mínima é inversamente proporcional ao ritmo de código, porque aumentamos o nr de bits redundantes.

pelas propriedades:

- Repetição (3, 1) - dmin = 3, t = 1, l = 2
- Paridade (3, 2) - dmin = 3, t = 0, l = 1
- Hamming (n, k) - dmin = 3, t = 1, l = 2 (SEMPRE)
- CRC para um grande valor de k (milhares de bits), coloca-se q bits redundantes.

### Códigos lineares de block (n, k)

- Bloco: todas as palavras têm a mesma dimensão
- Linear:
  - O vetor nulo pertence ao código.
  - A soma modular (XOR) de quaisquer duas palavras código é uma palavra código.
- Sistemática:
  - [mensagem | paridade]
- Não sistemática:
  -  não há separação entre mensagem e paridade
  - Os bits estão misturados.

### Peso de hamming

W(ci) = #bits a 1 da palavra de código ci
2^k palavras código de peso de hamming w

Fora o vetor nulo, após determinar o peso de hamming, o mínimo desses pesos é a distância mínima.
Com o código de hamming é linear, então dmin = min w(ci XOR cj) = min w(ck), sendo ck palavra do código, diferente do vetor nulo.

### Forma matricial de representação e utilização dos códigos de canal 

#### Descodificação: deteção

Síndroma - XOR dos bits de paridade recebidos com os bits de paridade calculados, se for nulo, não há erros, se não for nulo, há erros.

#### Descodificação: correção (FEC)

