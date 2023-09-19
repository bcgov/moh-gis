FROM maven:3.8.6-openjdk-11-slim as build-stage
RUN mkdir -p /home/gis
COPY gis/GIS-ear /home/gis/GIS-ear
COPY gis/GIS-ejb /home/gis/GIS-ejb
COPY gis/GIS-war /home/gis/GIS-war
COPY gis/pom.xml /home/gis

RUN mvn -f /home/gis/pom.xml clean package

FROM payara/server-full:5.2022.4-jdk11
RUN mkdir -p /opt/payara/.ssh
RUN chmod 700 /opt/payara/.ssh

# the two lines below can't be used in the github workflow because the files don't exist
# uncomment them for local development, but leave them commented in the repo
# COPY id_rsa /opt/payara/.ssh/id_rsa
# COPY known_hosts /opt/payara/.ssh/known_hosts

# use these five lines instead to get the keys from build args
# the github workflow needs these lines, comment them out for local development
ARG ID_RSA
ARG KNOWN_HOSTS
RUN echo $ID_RSA | base64 -d > /opt/payara/.ssh/id_rsa
RUN echo $KNOWN_HOSTS | base64 -d > /opt/payara/.ssh/known_hosts
RUN chmod 600 /opt/payara/.ssh/id_rsa /opt/payara/.ssh/known_hosts

COPY --from=build-stage /home/gis/GIS-ear/target/GIS-ear.ear $DEPLOY_DIR
COPY gis/pre-boot-commands.asadmin $PREBOOT_COMMANDS
COPY gis/postgresql-42.4.0.jar /opt/payara/appserver/glassfish/domains/domain1/lib
