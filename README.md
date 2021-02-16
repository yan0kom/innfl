# innfl
### Получение ИНН физ. лица по ФИО и дню рождения

Maven profiles:
* `jaxb` - сгенерировать jaxb классы по WSDL
* `frontend` - собрать и добавить в war фронтэнд  
  
собрать war для деплоя на Tomcat 8.5+  
`mvn clean package -Pjaxb,frontend`
