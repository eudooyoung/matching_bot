# 1. Slim한 OpenJDK 17 기반 이미지 사용
FROM openjdk:17.0-slim

# 2. Spring 프로파일 설정 (Jenkins나 환경에 따라 덮어씌울 수도 있음)
ENV SPRING_PROFILES_ACTIVE=prod

# 3. 빌드 결과 .jar 파일 복사 (build.gradle에서 이름 고정한 걸 기준)
COPY build/libs/matchingbotapp-1.0.0.jar app.jar

# 4. 타임존 설정 (Asia/Seoul)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 5. 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
