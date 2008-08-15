This tools for authentication native linux user in Java application.

1.Building.
 
  Run: 
  
  'mvn clean install antrun:run'
  
  In this case both Java and C source are builded!
  
  NOTE! Java and C library you can find in target folder. 

2. Test.

   NOTE: Before run script check it for right classpath! Put file src/main/conf/exo-jpam in folder /etc/pam.d.
   You must be able to read file /etc/shadow.

   For testing authenticator run script:

     './test-auth.sh _username_ _password_'

3. If you like to use this do the next steps:

   1. Place the exo.core.component.organization.pam-X.X.X.jar and 
      exo.core.component.security-X.X.X.jar into your classpath.
   2. Ensure that any libraries required to satisfy dependencies are also in the classpath.
   3. As an optional step, configure an appropriate logging level.
   4. Copy the native library libjpam.so to the Java Native Libary Path.
   5. Copy src/main/conf/exo-jpam in pam folder. On Linux /etc/pam.d. (If you didn't try the test, see n.2)
      Configure it like you want.
      