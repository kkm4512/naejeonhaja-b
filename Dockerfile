# Java 17 JDK를 포함한 경량 Alpine 이미지 사용
FROM openjdk:17-jdk-alpine

# 애플리케이션 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 로그 디렉터리 생성
RUN mkdir /logs
RUN chmod 777 /logs  # 권한 설정 (필수)

# 애플리케이션 기본 포트
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
