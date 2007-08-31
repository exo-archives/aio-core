#!/bin/sh

# NOTE! Change classpath and username/password fro your system.

java -cp /usr/maven2/repository/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
/usr/maven2/repository/org/exoplatform/kernel/exo.kernel.component.common/2.0.3/exo.kernel.component.common-2.0.3.jar:\
target/classes:.. org.exoplatform.services.organization.auth.pam.Pam username password
