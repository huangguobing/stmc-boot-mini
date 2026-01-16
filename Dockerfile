# =============================================
# 尚泰铭成 ERP 后端 Dockerfile (使用预构建的JAR)
# =============================================

FROM openjdk:8-jdk-slim

WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制已构建的JAR包
COPY stmc-server.jar app.jar

# 暴露端口
EXPOSE 48080

# JVM 参数（2G内存服务器，调小堆内存）
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=prod

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
