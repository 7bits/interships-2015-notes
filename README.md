# interships-2015-notes
GitHub RETARD test.
Необходимое программное обеспечение:
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
```sudo apt-get update```  
```sudo apt-get install software-properties-common```  
```sudo add-apt-repository ppa:webupd8team/java```  

```sudo apt-get update```  
```sudo apt-get install oracle-java8-installer```  
На все вопросы отвечаем да.<br>

<h5>2) Установка tomcat 7</h5>
```sudo apt-get install tomcat7```  
Откройте файлик /etc/default/tomcat7, найдите строку JAVA_HOME и впишите значение /usr/lib/jvm/java-8-oracle<br>

<h5>3) Установка maven 3</h5>
```sudo apt-get install maven```  

<h5>3) Установка postgresql 9.4</h5>
Создайте файлик /etc/apt/sources.list.d/pgdg.list и напишите в нем одно из следующего:<br>

Для ubuntu 14.04.<br>
```deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main```  
Для ubuntu 14.10.<br>
```deb http://apt.postgresql.org/pub/repos/apt/ utopic-pgdg main```  
Для ubuntu 12.04.<br>
```deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main```  

Выполните следующую команду:<br>
```wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -```  

```sudo apt-get update```
```sudo apt-get install postgresql-9.4```  

Создать базу данных notes, для этого зайдите в командный интерфейс субд под пользователем postgres.
(sudo su postgres -c psql postgres) и наберите следующие команды:  
 Вместо "<пароль>" напишите желаемый пароль для данного пользователя.  
 Вместо "<пользователь>" напишите имя желаемого пользователя.  

```CREATE ROLE <пользователь> WITH ENCRYPTED PASSWORD '<пароль>' LOGIN;```  
```CREATE DATABASE notes ENCODING 'UTF8' OWNER <пользователь>;```  
```GRANT ALL PRIVILEGES ON DATABASE notes TO <пользователь>;```  

Выходим из базы командой \q<br>

Далее в папке src/main/resources/config есть файл application_tpl.yml.  
Сделайте его копию с названием application.yml в той же папке.  
Откройте, и в пункте spring:datasource:username введите "<пользователь>" без кавычек.  
В пункте spring:datasource:password введите "<пароль>" без кавычек.  

Можно запускать приложение, в терминале перейдите в папку с файлом pom.xml с помощью команды cd "путь/до/проекта".<br>

Перейти в ветку develop с помощью команды git checkout develop<br>
Обновить ветку develop с помощью команды git pull origin develop<br>

Запускаем тесты:
```mvn package```

Запустить приложение можно с помощью команды:
```mvn clean spring-boot:run```

