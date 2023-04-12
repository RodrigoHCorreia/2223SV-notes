# Phase 2

- Em IPW usava-se Server Side Rendering:
  - Cada vez que há um pedido, o **servidor** vai buscar a página ao servidor e **devolve-a ao cliente**

- Em LS iremos usar Single Page Application:
  - Cada vez que há um pedido(AJAX), o servidor só produz dados (JSON), e o browser irá construir a página em conformidade.
  - fragmentos servem para identificar um segmento de html, dentro do mesmo documento, que iremos usar para identificar as diferentes páginas da nossa aplicação.
  - já não vem HTML após cada pedido, vem JSON.
- **event listener** - função que é chamada quando um evento ocorre.
- **getElementById** - função que devolve o elemento com o id especificado, pode ser uma div, um input, um botão, etc.
- **window.location.hash** - propriedade que devolve o fragmento da URL, ou seja, o que vem depois do #.
- 