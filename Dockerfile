FROM alpine:latest
RUN mkdir -p /repo
WORKDIR /repo
COPY .repo/ .
