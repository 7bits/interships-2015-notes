# interships-2015-notes
Needed technologies:
<ul>
<li>java8</li>
<li>tomcat7</li>
<li>maven3</li>
<li>postgresql9.4</li>
</ul>
<br>
<h4>Инструкция по запуску.</h4>
Тестировалось на ubuntu 14.04.

sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository ppa:webupd8team/java

sudo apt-get update
sudo apt-get install oracle-java8-installer
На все вопросы отвечаем да.

sudo apt-get install tomcat7
Откройте файлик /etc/default/tomcat7, найдите строку JAVA_HOME и впишите значение /usr/lib/jvm/java-8-oracle

sudo apt-get install maven

Создайте файлик /etc/apt/sources.list.d/pgdg.list и напишите в нем одно из следующего:

Для ubuntu 14.04.
deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main
Для ubuntu 14.10.
deb http://apt.postgresql.org/pub/repos/apt/ utopic-pgdg main
Для ubuntu 12.04.
deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main

Выполните следующую команду:
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -

sudo apt-get update
sudo apt-get install postgresql-9.4

Найти файлик pg_hba.conf в директории /etc/postgres/9.4/main/ для Linux,
Внизу файла поменять host all all 127.0.0.1/32 md5 на host all all 127.0.0.1/32 trust

sudo service postgresql restart

Создать базу данных notes, для этого зайдите в командный интерфейс субд под пользователем postgres.
(sudo su postgres -c psql postgres) и наберите CREATE DATABASE notes;

Можно запускать приложение, в терминале перейдите в папку с файлом pom.xml и наберите команду mvn spring-boot:run
