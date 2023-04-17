# Teoria da Informação

## Modelo de comunicação de Shannon

- A **fonte** produz uma sequência de símbolos a codificar.
- A **codificação da fonte** realiza codificação eficiente desses simbolos -> **Teorema da codificação da fonte**
- A **cifra** realiza encriptação dos bits a transmitir
- A **codificação do sinal** introduz redundância de forma a efetuar controlo de erros, para transmitir um canal ruidoso com probabilidade de erro arbitrariamente pequena -> **Teorema da codificação do sinal**
- **Informação própria** - associado a uma ocorrência de um símbolo (x) como 
  - I(xi) = -log2(p(xi)) [bit]
  
- **propriedades:**
  1) I(xi) = 0 se p(xi) = 1
  2) I(xi) >= 0 se 0 <= p(xi) <= 1 
  3) I(xi) > I(xj) se p(xi) > p(xj)
  4) I(xi, xk) = I(xi) + I(xk) se xi e xk são independentes

- **Entropia** de X é o valor esperado (médio) da informação própria de cada concretização:
  - H(X) = E[I(xi)] = -E[log2(p(xi))] = -sum(p(xi) * log2(p(xi))) [bit]

- **Entropia de fonte binária sem memória**
  - considerando uma fonte binária, em que um dos símbolos tem probabilidade a e o outro tem 1-a tem-se:
  - H(X) = a * log2(1/a) + (1-a) * log2(1/(1-a)) [bit]
  - A situação em que os dois símbolos são equiprováveis (a = 1/2) corresponde à maxima incerteza/expetativa.
  - a entropia varia entre 0 e log2(M), onde M é o número de concretizações possíveis.

## Codificação de fonte - Estatistica

- **Codificação de fonte** - é o processo de codificação de uma sequência de símbolos de uma fonte, de forma a reduzir a entropia da sequência de símbolos.
- Primeiro teorema de Shannon - **Teorema da codificação da fonte**
  - É possível codificar, sem disturção, uma fonte de entropia H usando uma média de L=H+epsilon bits por símbolo, em que epsilon é uma quantidade arbitrariamente pequena.

- A eficiência da codificação é dada por:
  - H(X)/L = H(X)/H(X) + epsilon 
  - em que L é o comprimento médio das palavras de código e H(X) é a entropia da fonte.
  - epsilon corresponde à redundância do código.

- **Comprimento médio - L** - L = sum(p(xi) * l(c(xi))) [bit]
  - l(c(xi)) - comprimento da palavra de código do símbolo xi em número de bits
  - p(xi) - probabilidade do símbolo xi
  - representa o número de bits que é necessário para codificar cada símbolo da fonte.

### Algoritmos estatísticos de codificação de fonte

- **Algoritmo de geração de código unário (comma code)**
    1) Ordenar os símbolos por ordem decrescente de probabilidade
    2) Realizar a atribuição sucessiva das configuração binárias:
       1) 0
       2) 10
       3) 110
       4) 1110
       5) ...

- **Algoritmo de geração de código de Huffman**
    1) Ordenar os símbolos por ordem decrescente de probabilidade e considera-los como nós duma arvore
    2) Enquanto houver mais do que um nó:
       1) Agrupar os dois nós com menor probabilidade
       2) Arbitrariamente atribuir 0 ao nó de maior probabilidade e 1 ao nó de menor probabilidade
    - O código de Huffman é _ótimo_ porque garante que H(X) <= L <= H(X) + 1 
    - No caso em que H(X) = L, o código de Huffman é _ideal_.
    
<img src="../CD/docs/estudo/Huffman.png>

- **Algoritmo de geração de código de Shannon-Faro**
    1) Ordenar os símbolos por ordem decrescente de probabilidade
    2) Considerar todos os símbolos para a alocação inicial de bits
    3) De forma recursiva, dividir o conjunto de símbolos em dois subconjuntos, aproximadamente com a mesma probabilidade
       1) Atribuir bit 1 a uma parte
       2) Atribuir bit 0 a outra parte
    4) Proceder assim, até todas as partes conterem apenas um símbolo

#### Formas de compressão

- Existem 3 formas de compressão comuns:
    1) Estática
    2) Semi-adaptativa
    3) Adaptativa

- **Forma estática :**
  - O codificador e o decodificador usam o mesmo modelo, estabelecido à priori
  - Este modelo não é calculado conforme o ficheiro a codificar/decodificar
  - O modelo não é modificado durante a codificação/decodificação

- **Forma semi-adaptativa :**
  - O codificador e o decodificador usam o mesmo modelo
  - Este modelo é calculado, pelo codificador conforme o ficheiro a codificar
  - O modelo é escrito no ficheiro codificado
  - O decodificador dá início às suas ações, começando por ler o modelo a partir do ficheiro codificado; em seguida, começa a decodificar o ficheiro, tendo por base o modelo lido

- **Forma adaptativa :**
  - o codificador e o decodificador usam o mesmo modelo
  - Tal como na forma estática, o modelo inicial é usado pelo codificador e pelo decodificador
  - O codificador e o decodificador vão atualizando o modelo, com as mesmas regras, à medida que o processo de codificação/decodificação decorre

## Codificação de fonte - dicionários

A = {a, b}, seja a **fonte** que produz símbolos do alfabeto
M = {a, b, ab, ba}, o **conjunto de palavras**
C = {C(a) = 111, C(b) = 110, C(ab) = 10, C(ba) = 0}, o **conjunto de códigos**

Então **D(M, C)** diz-se um codificador baseado em dicionário

- Sequências de símbolos são codificadas através de sequências de tokens/tuplos
- Cada token/tuplo:
  - Codifica um número variável de símbolos
  - Ocupa um número fixo de bits

## Algoritmo de codificação de fonte baseado em dicionário

### LZ77

- **LZ77** - é um algoritmo de codificação de fonte baseado em dicionário
- Usa uma janela deslizante com:
  - **Dicionário(texto anterior)**
  - **Look-ahead buffer (LAB) ou janela de codificação**

- Procura-se no dicionário, por uma sequência de símbolos que se repete no LAB

- As referências ao texto anterior são representadas na forma de tokens (tuplos) com a designação
    - **(posição, comprimento, símbolo)**
    - **posição** - indica a posição da substring no dicionário
    - **length** - representa o número de símbolos em comum
    - **innovation_symbol** - é o primeiro símbolo que quebra a pesquisa
- Caso não exista qualquer ocorrência no dicionário, gera-se o token (0, 0, símbolo)
- a posição 0 sinaliza que não existe nenhuma ocorrência do símbolo no dicionário
- Cada token ocupa log2(|D|) + log2(|J|) + 8 bits
    - |D| - número de posições do dicionário
    - |J| - número de posições do LAB

 ### LZSS

 - **LZSS** - é um algoritmo de codificação de fonte baseado em dicionário **mais eficiente**
 - O formato do token é modificado para
   - Flag_bit(position, length)
   - Flag_bit symbol

- Por exemplo:
  - 1 (position, length)
  - 0 symbol
ou 
  - 0 (position, length)
  - 1 symbol

---

Combinação de LZ Encoding + Huffman Coding = DEFLATE

Combinação de LZ Decoding + Huffman Decoding = INFLATE

### Medidas de avaliação e compressão
- do, dimensão do ficheiro original
- dc, dimensão do ficheiro codificado

- Razão ou taxa de compressão, dc/do * 100 [%]
- Percentagem removida, (1-dc/do) * 100 [%]
- Bit por byte, dc/do * 8 [bpb]
- N:1, Proporção de N símbolos do ficheiro original que são codificados com um símbolo do ficheiro de saída, em média, 1/N = dc/do

