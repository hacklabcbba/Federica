set SCRIPT_DIR=%~dp0
java -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=512m -Xmx1024M -Xss2M -Dfile.encoding=UTF-8 -jar "%SCRIPT_DIR%\sbt-launch-0.13.1.jar" %*
