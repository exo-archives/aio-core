@if "%M2_REPO%"=="" goto error
@goto ok

:error
@echo ERROR: set M2_REPO first
@goto end


:ok
@set CLASSPATH=%CLASSPATH%%M2_REPO%\org\exoplatform\core\exo.core.component.security.core\2.1.6-SNAPSHOT\exo.core.component.security.core-2.1.6-SNAPSHOT.jar;
@set CLASSPATH=%CLASSPATH%%M2_REPO%\org\exoplatform\kernel\exo.kernel.component.common\2.0.8-SNAPSHOT\exo.kernel.component.common-2.0.8-SNAPSHOT.jar;
@set CLASSPATH=%CLASSPATH%%M2_REPO%\commons-logging\commons-logging\1.1\commons-logging-1.1.jar;
@set CLASSPATH=%CLASSPATH%%M2_REPO%\jcifs\jcifs\1.2.17\jcifs-1.2.17.jar;

@set CLASSPATH=%CLASSPATH%.\target\classes;
@set CLASSPATH=%CLASSPATH%.\target\test-classes;.

java org.exoplatform.services.security.ntlm.NTLMAuthenticatorTest %1 %2

@goto end


:end