#!/bin/sh
# NOTE! Edit classpath for you system.

export MAVEN_REPO=$HOME/lib/exo/maven-repo

java -cp $MAVEN_REPO/org/exoplatform/core/exo.core.component.organization.api/2.0.3/exo.core.component.organization.api-2.0.3.jar:\
$MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/2.0.3/exo.kernel.component.common-2.0.3.jar:\
target/classes:\
target/test-classes:. \
-Djava.security.auth.login.config=./src/main/conf/jpam-jaas.config \
-Djava.library.path=./target/c \
org.exoplatform.services.organization.auth.pam.jaas.JPAMTestJAAS
