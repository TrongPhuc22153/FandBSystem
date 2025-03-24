FROM openjdk:17
COPY target/phucxfoodshop-0.0.1-SNAPSHOT.jar phucxfoodshop-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
VOLUME [ "/usr/src/app/image/customer" ]
VOLUME [ "/usr/src/app/image/employee" ]
VOLUME [ "/usr/src/app/image/product" ]
VOLUME [ "/usr/src/app/image/category" ]

EXPOSE 8081

ENTRYPOINT [ "bash", "./entrypoint.sh" ]
