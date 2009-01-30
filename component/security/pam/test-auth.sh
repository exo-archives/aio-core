#!/bin/sh
# NOTE! Edit classpath for you system.

export MAVEN_REPO=$HOME/exo/exo-dependencies/repository

$JAVA_HOME/bin/java -cp \
$MAVEN_REPO/org/exoplatform/core/exo.core.component.security.core/2.1.4/exo.core.component.security.core-2.1.4.jar:\
$MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/2.0.6/exo.kernel.component.common-2.0.6.jar:\
target/classes:\
target/test-classes:. \
-Djava.library.path=./target/c \
org.exoplatform.services.security.pam.JPamAuthenticatorTest $1 $2
