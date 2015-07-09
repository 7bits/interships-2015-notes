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
<p>После установки перечисленного выше ПО вам необходимо выполнить следущее:
    <p><strong>1)</strong> Найти файлик pg_hba.conf в директории /etc/postgres/9.4/main/ для Linux
    внизу файла поменять host    all             all             127.0.0.1/32            md5
    на host    all             all             127.0.0.1/32            trust</p>
    <p><strong>2)</strong> Перезагрузить postgres, например командой sudo service postgresql restart</p>
    <p><strong>3)</strong> Создать базу данных notes, для этого зайдите в командный интерфейс субд под пользователем postgres.
    (sudo su postgres -c psql postgres) и наберите CREATE DATABASE notes;</p>

    Можно запускать приложение, в терминале перейдите в папку с файлом pom.xml и наберите команду mvn spring-boot:run
</p>
