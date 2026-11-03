FROM node:20-alpine AS frontend-build
WORKDIR /workspace/frontend

COPY ProSigaa/package*.json ./
RUN npm ci

COPY ProSigaa/ ./
RUN npm run build

FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /workspace/backend

COPY backend/gradlew backend/gradlew.bat backend/settings.gradle backend/build.gradle ./
COPY backend/gradle ./gradle
COPY backend/src ./src

RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache nginx \
    && mkdir -p /run/nginx /app/data

COPY docker/nginx.conf /etc/nginx/http.d/default.conf
COPY docker/start.sh /start.sh
COPY --from=frontend-build /workspace/frontend/dist /usr/share/nginx/html
COPY --from=backend-build /workspace/backend/build/libs/*.jar /app/app.jar

RUN chmod +x /start.sh

EXPOSE 80
ENTRYPOINT ["/start.sh"]
