maven -Pnotest tomcat7:redeploy
gulp css-staging
scp -r src/main/resources/public notes@192.168.200.112:/home/notes/Notes/target/classes/
