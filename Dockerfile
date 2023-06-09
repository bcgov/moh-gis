FROM maven:3.8.6-openjdk-11-slim as build-stage
RUN mkdir -p /home/gis
RUN mkdir -p /opt/payara/.ssh
COPY errorhandler-1.0.jar /home/gis
COPY HNSharedCode-5.0.0.jar /home/gis
COPY HNSharedCode-5.0.0.pom /home/gis
RUN mvn install:install-file -Dfile=/home/gis/errorhandler-1.0.jar -DgroupId=com.cgi.gis -DartifactId=errorhandler -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
RUN mvn install:install-file -Dfile=/home/gis/HNSharedCode-5.0.0.jar -DpomFile=/home/gis/HNSharedCode-5.0.0.pom -DgroupId=ca.bc.gov.health -DartifactId=HNSharedCode -Dversion=5.0.0 -Dpackaging=jar
COPY GIS-ear /home/gis/GIS-ear
COPY GIS-ejb /home/gis/GIS-ejb
COPY GIS-war /home/gis/GIS-war
COPY pom.xml /home/gis

RUN mvn -f /home/gis/pom.xml clean package -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.insecure=true

ARG ID_RSA
ARG KNOWN_HOSTS
RUN echo $ID_RSA > /opt/payara/.ssh/id_rsa
RUN echo $KNOWN_HOSTS > /opt/payara/.ssh/known_hosts
RUN chmod 640 /opt/payara/.ssh/id_rsa /home/gfadmin/.ssh/known_hosts

FROM payara/server-full:5.2022.4-jdk11
COPY pre-boot-commands.asadmin $PREBOOT_COMMANDS
#COPY post-boot-commands.asadmin $POSTBOOT_COMMANDS

COPY --from=build-stage /home/gis/GIS-ear/target/GIS-ear.ear $DEPLOY_DIR
COPY postgresql-42.4.0.jar /opt/payara/appserver/glassfish/domains/domain1/lib
