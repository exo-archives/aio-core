#!/bin/sh
# NOTE! Edit classpath for you system.

export MAVEN_REPO=$HOME/lib/exo/maven-repo

java -cp $MAVEN_REPO/org/exoplatform/core/exo.core.component.organization.api/trunk/exo.core.component.organization.api-trunk.jar:\
$MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/trunk/exo.kernel.component.common-trunk.jar:\
target/classes:\
target/test-classes:. \
-Djava.security.auth.login.config=./src/main/conf/jpam-jaas.config \
-Djava.library.path=./target/c \
org.exoplatform.services.organization.auth.pam.jaas.JPAMTestJAAS
