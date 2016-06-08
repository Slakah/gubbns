FROM java:8-alpine

RUN apk add --update bash && rm -rf /var/cache/apk/*

COPY target/universal /opt/play

WORKDIR /opt/play

RUN unzip *.zip -d tmp/ \
  && mv tmp/*/* . \
  && rm -rf tmp/

ENV PATH=${PATH}:/opt/play/bin
