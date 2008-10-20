echo
echo "WebMail Build System"
echo "-------------------------"
echo "Contributed by Nathan Chandler"
echo

if not "%JAVA_HOME%" == "" goto CONT 
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  goto END
fi
:CONT
WEBMAIL_BASE=.

set TOMCAT_HOME=e:\jakarta-tomcat-4.0
set CLASSPATH=%JAVA_HOME%/lib/tools.jar:%WEBMAIL_BASE%/contrib/xerces.jar;%TOMCAT_HOME%/common/lib/servlet.jar;%WEBMAIL_BASE%/contrib/jakarta-regexp-1.2.jar;%JAVA_HOME%/lib/dev.jar

echo Building with classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

rem set CLASSPATH=$LOCALCLASSPATH:$ADDITIONALCLASSPATH
ant %1 %2 %3


:END
