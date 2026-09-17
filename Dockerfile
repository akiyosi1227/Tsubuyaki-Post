# MavenとJava 21を使ってWARファイルを作成
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# 依存関係の設定を先にコピー
COPY pom.xml .

# 必要なライブラリを取得
RUN mvn dependency:go-offline

# アプリケーションのソースコードをコピー
COPY src ./src

# ROOT.warを作成
RUN mvn clean package -DskipTests


# Tomcat 10とJava 21でアプリケーションを実行
FROM tomcat:10.1-jdk21-temurin

# 標準のWebアプリケーションを削除
RUN rm -rf /usr/local/tomcat/webapps/*

# Mavenで作成したWARをTomcatへ配置
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Tomcatが使用するポート
EXPOSE 8080

# Tomcatを起動
CMD ["catalina.sh", "run"]
