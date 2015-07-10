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
<h5>1) Установка java 8</h5>
sudo apt-get update<br>
sudo apt-get install software-properties-common<br>
sudo add-apt-repository ppa:webupd8team/java<br>

sudo apt-get update<br>
sudo apt-get install oracle-java8-installer<br>
На все вопросы отвечаем да.<br>

<h5>2) Установка tomcat 7</h5>
sudo apt-get install tomcat7<br>
Откройте файлик /etc/default/tomcat7, найдите строку JAVA_HOME и впишите значение /usr/lib/jvm/java-8-oracle<br>

<h5>3) Установка maven 3</h5>
sudo apt-get install maven

<h5>3) Установка postgresql 9.4</h5>
Создайте файлик /etc/apt/sources.list.d/pgdg.list и напишите в нем одно из следующего:<br>

Для ubuntu 14.04.<br>
deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main<br>
Для ubuntu 14.10.<br>
deb http://apt.postgresql.org/pub/repos/apt/ utopic-pgdg main<br>
Для ubuntu 12.04.<br>
deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main<br>

Выполните следующую команду:<br>
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -<br>

sudo apt-get update<br>
sudo apt-get install postgresql-9.4<br>

Найти файлик pg_hba.conf в директории /etc/postgres/9.4/main/ для Linux,<br>
Внизу файла поменять host all all 127.0.0.1/32 md5 на host all all 127.0.0.1/32 trust<br>

sudo service postgresql restart<br>

Создать базу данных notes, для этого зайдите в командный интерфейс субд под пользователем postgres.
(sudo su postgres -c psql postgres) и наберите CREATE DATABASE notes;

Можно запускать приложение, в терминале перейдите в папку с файлом pom.xml и наберите команду mvn spring-boot:run
