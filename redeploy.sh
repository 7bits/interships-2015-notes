maven -Pnotest tomcat7:redeploy
gulp build-staging
scp -r src/main/resources/public root@192.168.200.112:/home/notes/Notes/target/classes/
