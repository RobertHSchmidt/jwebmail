$Id$

This file is for people who want to develop JWebMail itself, or to
build it.  People who just want to run the JWebMail application should
work from a binary installation, which will have pre-built documentation.

You need Java, Ant, and Ivy installed to bootstrap everything else.
Briefly, install a Java JDK and set the JAVA_HOME variable to the JDK
installation root directory; install Ant and make sure ant (or ant.bat)
is in your search path; and put a v. 2.* Ivy jar file in your CLASSPATH.
You can then run the ant commands.

Once you have those prerequisites taken care of, run "ant generate-readme"
to generate the kind of documentation that you are probably looking for.
If you don't have "xsltproc", you can generate the README.html file manually
with your favorite XSLT processor tool and the artifacts in the
src/doc/readme directory.
