FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/user-service-1.0.jar UserService.jar
ENTRYPOINT ["java", "-jar", "UserService.jar"]

# docker run -d --network ecommerce-network `
#     --name user-service `
#     -e "spring.cloud.config.uri=http://config-service:8888" `
#     -e "spring.rabbitmq.host=rabbitmq" `
#     -e "spring.zipkin.base-url=http://zipkin:9411" `
#     -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" `
#     -e "logging.file=/api-logs/users-ws.log" `
#     mmsnow/user-service:1.0