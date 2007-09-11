#!/bin/sh

# NOTE! Change classpath and username/password fro your system.

export MAVEN_REPO=/usr/maven2/repository

java -cp $MAVEN_REPO/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPO/org/exoplatform/kernel/exo.kernel.component.common/2.0.3/exo.kernel.component.common-2.0.3.jar:\
target/classes:.. org.exoplatform.services.organization.auth.pam.Pam $1 $2
