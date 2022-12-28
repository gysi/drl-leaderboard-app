FROM alpine:3
WORKDIR /app
RUN apk add fontconfig-dev freetype-dev
COPY DRLLeaderboardBackend /app/DRLLeaderboardBackend

ENTRYPOINT ["/app/DRLLeaderboardBackend"]
