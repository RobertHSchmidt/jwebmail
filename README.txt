$Id$

This file is for people who want to develop JWebMail itself, or to
build it.  People who just want to run the JWebMail application should
work from a binary installation, which will have pre-built documentation.

IDE developers just need to configure their Ant invoker to include
the jar file in the bootstrap-libs subdirectory in the CLASSPATH.

Non-IDE developers need Java and Ant, and the supplied Ivy jar file 
in your CLASSPATH, in order to bootstrap everything else.
Briefly
    + Install a Java JDK 1.5 or laterand set the JAVA_HOME variable to
      the JDK installation root directory
    + Install Ant and make sure ant (or ant.bat) is in your search path
    + When you run an Ant target which needs Ivy to find libraries,
      instructions will be displayed for a single command to tell Ant
      how to find the Ivy jar file.

The ANT_ARGS setting is just a suggestion.  The critical thing is to
get the Ivy jar file into your Ant CLASSPATH.  You could, alternatively,
copy the file to your $HOME/.ant/lib directory, set your CLASSPATH
variable, use Windows' Control Panel / System / Advanced / Env vars to
set an env. variable using an absolute path, etc.  For better maintenance
and scalability, I suggest that you add the bootstrap-libs content
instead of hard-coding the single jar file name (for one thing, the
filename will change when we upgrade it; for another, it's possible that
additional bootstrap libs could become necessary in the future).

Once you have those prerequisites taken care of, use your IDE or "ant -p"
to list the available Ant targets (with Eclipse, click the Target icon
to hide internal targets... why would you ever want to see those?).
You will want to execute the "generate-readme" target to generate the kind
of documentation that you are probably looking for.
If you don't have "xsltproc", you can generate the README.html file manually
with your favorite XSLT processor tool and the artifacts in the
src/doc/readme directory.
