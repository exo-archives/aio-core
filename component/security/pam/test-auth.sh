#!/bin/sh
# NOTE! Edit classpath for you system.

export MAVEN_REPO=$HOME/lib/exo/maven-repo

java -cp $MAVEN_REPO/org/exoplatform/core/exo.core.component.security/2.1/exo.core.component.security-2.1.jar:\
$MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/trunk/exo.kernel.component.common-trunk.jar:\
target/classes:\
target/test-classes:. \
-Djava.library.path=./target/c \
org.exoplatform.services.security.pam.JPamAuthenticatorTest $1 $2
