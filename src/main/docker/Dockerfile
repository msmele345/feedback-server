FROM amazoncorretto:17
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]


##deploy app using gradle plugin
# create system assigned identity
# go to key vault and add the web app identity
#make sure app settings are set to 8080