#!/bin/sh

# NOTE! Change classpath and username/password fro your system.

export MAVEN_REPO=$HOME/lib/exo/maven-repo


java -cp $MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/trunk/exo.kernel.component.common-trunk.jar:\
target/classes:.. \
-Djava.library.path=./target/c \
org.exoplatform.services.organization.auth.pam.Pam $1 $2
