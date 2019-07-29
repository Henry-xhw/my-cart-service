FROM mcr.microsoft.com/mssql/server:2017-latest

ENV ACCEPT_EULA=Y
ENV SA_PASSWORD=123!P01@W
ENV MSSQL_PID=Developer

ARG TARGET_DIR=cart_init

EXPOSE 1433

COPY $TARGET_DIR ./$TARGET_DIR
COPY scripts ./$TARGET_DIR/sql/

WORKDIR $TARGET_DIR
RUN chmod +x ./*.sh
CMD ./$TARGET_DIR/entrypoint.sh