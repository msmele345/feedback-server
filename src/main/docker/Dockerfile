FROM amazoncorretto:17
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]


##deploy app using gradle plugin
# create system assigned identity for webapp 
# go to key vault and add the web app identity (Access Control IAM > Role assignments). Create access policy for app in keyvault access controls?
#make sure app settings are set to 8080
#port 8080 in dockerfile and both PORT and WEBSITED_PORT in app settings to 8080 made it work