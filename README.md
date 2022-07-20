Для запуска приложения с помощью контейнеров воспользуйтесь слудующей очередностью команд:

1. docker pull mysql:8.0
2. docker pull pihota1997/message-system
3. docker network create message-network
4. docker run --name mysqldb --network message-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=messagedb -e MYSQL_USER=myqsl -e MYSQL_PASSWORD=mysql -p 3316:3306 -d mysql:8.0
5. docker run --network message-network --name message-system -p 9090:9090 -d pihota1997/message-system

Также можно воспользоваться приложенным docker-compose.yml.

______________________________________________________________________________________________________________________
При запуске приложения через IDE необходимо:
1. создать базу данных mysql со следующими параметрами:
   - username = myqsl
   - password = mysql
   - databasename = messagedb
   - port: 3316 

В случае необходимости указать другие параметры для подключения к БД, внесите соотвествующие правки в application.properties

2. запустить приложение

______________________________________________________________________________________________________________________
Примеры requests указаны в requests.http
