FROM maven:3.8.6-openjdk-11-slim as build-stage
RUN mkdir -p /home/gis
RUN mkdir -p /home/gfadmin/.ssh
COPY gis/errorhandler-1.0.jar /home/gis
COPY gis/HNSharedCode-5.0.0.jar /home/gis
COPY gis/HNSharedCode-5.0.0.pom /home/gis
RUN mvn install:install-file -Dfile=/home/gis/errorhandler-1.0.jar -DgroupId=com.cgi.gis -DartifactId=errorhandler -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
RUN mvn install:install-file -Dfile=/home/gis/HNSharedCode-5.0.0.jar -DpomFile=/home/gis/HNSharedCode-5.0.0.pom -DgroupId=ca.bc.gov.health -DartifactId=HNSharedCode -Dversion=5.0.0 -Dpackaging=jar
COPY gis/GIS-ear /home/gis/GIS-ear
COPY gis/GIS-ejb /home/gis/GIS-ejb
COPY gis/GIS-war /home/gis/GIS-war
COPY gis/pom.xml /home/gis

RUN mvn -f /home/gis/pom.xml clean package -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.insecure=true

# the two lines below can't be used in the github workflow because the files don't exist
# uncomment them for local development, but leave them commented in the repo
# COPY id_rsa_DEV /home/gfadmin/.ssh/id_rsa
# COPY known_hosts /home/gfadmin/.ssh/known_hosts

# use these five lines instead to get the keys from build args
# the github workflow needs these lines, comment them out for local development
ARG ID_RSA
ARG KNOWN_HOSTS
RUN echo $ID_RSA | base64 -d > /home/gfadmin/.ssh/id_rsa
RUN echo $KNOWN_HOSTS | base64 -d > /home/gfadmin/.ssh/known_hosts
RUN chmod 640 /home/gfadmin/.ssh/id_rsa /home/gfadmin/.ssh/known_hosts

FROM payara/server-full:5.2022.4-jdk11
RUN mkdir -p /opt/payara/.ssh
COPY gis/pre-boot-commands.asadmin $PREBOOT_COMMANDS

COPY --from=build-stage /home/gis/GIS-ear/target/GIS-ear.ear $DEPLOY_DIR
COPY --from=build-stage /home/gfadmin/.ssh/* /opt/payara/.ssh
COPY gis/postgresql-42.4.0.jar /opt/payara/appserver/glassfish/domains/domain1/lib