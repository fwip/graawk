FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22.0.0.2

ARG MAVEN_VERSION=3.8.6

RUN cd /tmp \
  && curl https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz -o maven.tar.gz \
  && tar xf maven.tar.gz \
  && cp -lr  apache-maven-$MAVEN_VERSION/* /usr/local/ \
  && rm -rf apache-maven-$MAVEN_VERSION maven.tar.gz

RUN gu install native-image

RUN adduser vscode
USER vscode