FROM ubuntu as build

ENV BASE_DIR="/java-tron"
ENV JDK_TAR="jdk-8u202-linux-x64.tar.gz"
ENV JAVA_DIR="jdk1.8.0_202"

RUN apt-get update \
    && apt-get install -y wget unzip \
    && cd /usr/local \
    && wget https://github.com/frekele/oracle-java/releases/download/8u202-b08/$JDK_TAR \
    && tar -zxf /usr/local/$JDK_TAR -C /usr/local

COPY . $BASE_DIR
RUN cd $BASE_DIR \
    && export JAVA_HOME=/usr/local/$JAVA_DIR \
    && export PATH="$JAVA_HOME/bin:$PATH" \
    && echo "PATH=$PATH" > /etc/environment \
    && echo "JAVA_HOME=$JAVA_HOME" >> /etc/environment\
    && ./gradlew build -x test

FROM openjdk:8
ENV BASE_DIR="/java-tron"
COPY --from=build $BASE_DIR/build/libs/FullNode.jar $BASE_DIR/
COPY docker-entrypoint.sh /usr/bin
RUN chmod +x /usr/bin/docker-entrypoint.sh
WORKDIR $BASE_DIR

ENTRYPOINT ["docker-entrypoint.sh"]