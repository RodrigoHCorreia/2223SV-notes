# Aula 8 - Logger cont.

- através de @AltName - podemos configurar o nome da propriedade
  -  Estático para cada objeto
- através de @?? - poder configurar o valor da propriedade
  - Dinâmico.
  - Eg. num número configurar casas decimais, em string dizer se é maiúscula ou minúscula, formatar data, etc.

- Em java um tipo `função` é implementado à custa de uma interface

```java
@Format(parameter = FormatStringToUpper.class) 

interface Formatter {
    String format(Ojbect v);
}

class FormatStringToUpper implements Formatter {
    public st_format(Object v) {
        return ((String)v).upperCase()
    }
}
```
- **Propriedades de Anotações podem ser de tipo:**
  - `Primitive`
  - `String`
  - `.Class`
  - `Array Dos tipos Anteriores`

- Muito importante o parametero ser .class e não new FormatStringToUpper(), porque este gera uma instância de uma classe e só são admitidos .class

# Posters

- Quando temos acesso à interface sem ser necessário utilizar reflexão, devemos utilizar sempre a interface.
- só usamos reflexão para aceder a coisas que não temos acesso.