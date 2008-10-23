$Id$

This file is for people who want to develop JWebMail itself, or to
build it.  People who just want to run the JWebMail application should
work from a binary installation, which will have pre-built documentation.

IDE developers just need to configure their Ant invoker to include
the jar file in the bootstrap-libs subdirectory in the CLASSPATH.

Non-IDE developers need Java and Ant, and the supplied Ivy jar file 
in your CLASSPATH, in order to bootstrap everything else.
Briefly
    + Install a Java JDK and set the JAVA_HOME variable to the JDK
      installation root directory
    + Install Ant and make sure ant (or ant.bat) is in your search path
    + Set env. variable ANT_ARGS like so.  Bourne-compatible shells:
          export ANT_ARGS; ANT_ARGS='-lib bootstrap-libs -noclasspath'
        C-compatible shells
          setenv ANT_ARGS '-lib bootstrap-libs -noclasspath'
        Windows CMD shell
          SET ANT_ARGS=-lib bootstrap-libs -noclasspath
        Or, use Windows' Control Panel / System / Advanced / Env. Vars
        to set the value of variable "ANT_ARGS" to
          "-lib bootstrap-libs -noclasspath" (without the quotes).
The ANT_ARGS setting is just a suggestion.  The critical thing is to
get the Ivy jar file into your Ant CLASSPATH.  You could, alternatively,
copy the file to your $HOME/.ant/lib directory, set your CLASSPATH
variable, etc.

IMPORTANT:  If you run Ant and get an error message like
"...failed to create task... antlib:org.apache.ivy... The name is undefined..."
then Ant is not finding the Ivy jar file.  If in UNIX, did you export your
ANT_ARGS or CLASSPATH variable?

Once you have those prerequisites taken care of, use your IDE or "ant -p"
to list the available Ant targets (with Eclipse, click the Target icon
to hide internal targets... why would you ever want to see those?).
You will probably want to execute the "generate-readme" target
to generate the kind of documentation that you are probably looking for.
If you don't have "xsltproc", you can generate the README.html file manually
with your favorite XSLT processor tool and the artifacts in the
src/doc/readme directory.
