# thymeleafdemo

Demo Thymeleaf + Spring MVC + Generación PDF

En este ejemplo se ha evolucionado el código de https://github.com/sebascastillo89/thymeleafdemo para generar un informe pdf con la

  Instrucciones:

1) Importar proyecto desde el IDE preferido como Proyecto Maven
2) Ejecutar es.secaro.thymeleafdemo.ThymeleafdemoApplication
3) Acceder a esta URL http://localhost:8080/raffle, seleccionar participantes con puntuación y hacer click en "Sortear".
4) Hacer click en botón "Download PDF" y se generará un fichero pdf con los resultados del sorteo

Para ello se ha utilizado la librería Flying Saucer: https://github.com/flyingsaucerproject/flyingsaucer

Se trata de una librería Java que es capaz de generar paneles Swing, PDF e imágenes a partir de plantillas XML (or XHTML) usando CSS 2.1 bien formadas.

En concreto se ha utizado la librería flying-saucer-pdf para la generación del pdf.



