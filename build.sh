#!/bin/sh
# $Id$
#
# Copyright 2008 by the JWebMail Development Team and Sebastian Schaffert.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

VERSION=`grep "name=\"version\"" build.xml | cut --delimiter="\"" -f 4`

echo
echo "WebMail $VERSION Build System"
echo "-------------------------"
echo "Contributed by Nathan Chandler and GCS, Linux Support Center"
echo

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi
WEBMAIL_BASE=.

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:$WEBMAIL_BASE/contrib/xercesImpl.jar:$WEBMAIL_BASE/contrib/servlet-2.3.jar:$WEBMAIL_BASE/contrib/ant.jar:$WEBMAIL_BASE/contrib/jakarta-oro-2.0.6.jar:$JAVA_HOME/lib/dev.jar
ANT_HOME=./contrib
SERVLET_CONTAINER=/usr/share/tomcat

if [ "$1" = "install-catalina" ]; then
    echo Target: Catalina
    if [ "$CATALINA_HOME" != "" ]; then
        SERVLET_CONTAINER=$CATALINA_HOME
    fi
else
    echo Target: Tomcat
    if [ "$TOMCAT_HOME" != "" ]; then
        SERVLET_CONTAINER=$TOMCAT_HOME
    fi
fi

echo Building with classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dwebapp.home=$SERVLET_CONTAINER/webapps/ -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH org.apache.tools.ant.Main $*
