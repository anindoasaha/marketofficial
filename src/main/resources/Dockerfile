FROM openjdk:11-slim as builder

WORKDIR /app

COPY . .
ENV PATH="/app:${PATH}"
RUN echo $PATH; ls; chmod +x protoc-gen-grpc-web; chmod +x gradlew; ./gradlew installDist

FROM openjdk:13-alpine

RUN apk add --no-cache libc6-compat

WORKDIR /app
COPY --from=builder /app .

EXPOSE 9090
ENTRYPOINT ["/app/build/install/marketofficial/bin/marketofficial"]