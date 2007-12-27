This tools for authentication native linux user in Java application.

1.Building.
 
  Run: 
  
  'mvn clean install antrun:run'
  
  In this case both Java and C source are builded!
  
  NOTE! Java and C library you can find in target folder. 

2. Test.

   NOTE: Before run script check it for right classpath! Put file src/main/conf/exo-jpam in folder /etc/pam.d.
   You must be able to read file /etc/shadow.

   For test builded and configured jpam tools run:

     './test-jpam.sh _username_ _password_' 

  For testing JAAS module run script:

     './test-jaas.sh'

3. If you like to use this do the next steps:

   1. Place the exo.core.component.organization.pam-X.X.X.jar into your classpath.
   2. Ensure that any libraries required to satisfy dependencies are also in the classpath.
   3. As an optional step, configure an appropriate logging level.
   4. Copy the native library libjpam.so to the Java Native Libary Path.
   5. Copy src/main/conf/exo-jpam in pam folder. On Linux /etc/pam.d. (If you didn't try the test, see n.2)
      Configure it like you want.
   6. If you going to use JAAS authentication put file src/main/conf/jpam-jaas.config
      in place what you want, and remember add
      -Djava.security.auth.login.config=__path_to_jpam-jaas.config__ when run application. 
      