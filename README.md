��� ������� ���������� � ������� ����������� �������������� ��������� ������������ ������:

1. docker pull mysql:8.0
2. docker pull pihota1997/message-system
3. docker network create message-network
4. docker run --name mysqldb --network message-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=messagedb -e MYSQL_USER=myqsl -e MYSQL_PASSWORD=mysql -p 3316:3306 -d mysql:8.0
5. docker run --network message-network --name message-system -p 9090:9090 -d pihota1997/message-system

����� ����� ��������������� ����������� docker-compose.yml. �������������� ���������� ������� pihota1997/message-system (��.�.2)

______________________________________________________________________________________________________________________
��� ������� ���������� ����� IDE ����������:
1. ������� ���� ������ mysql �� ���������� �����������:
   - username = myqsl
   - password = mysql
   - databasename = messagedb
   - port: 3316
   � ������ ������������� ������� ������ ��������� ��� ����������� � ��, ������� �������������� ������ � application.properties
2. ��������� ����������

______________________________________________________________________________________________________________________
������� requests ������� � requests.http