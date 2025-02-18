FROM alpine:latest
COPY . .
RUN mkdir -p /repo
WORKDIR /repo
COPY .repo/ /repo
