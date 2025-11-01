FROM mysql:9.5.0

RUN rm -rf /etc/localtime \
&& ln -s /usr/share/zoneinfo/Europe/Zurich /etc/localtime \
&& cp -r /var/lib/mysql /var/lib/mysql-no-volume

ENV MYSQL_ROOT_PASSWORD="password" \
    MYSQL_DATABASE="mediadb" \
    MYSQL_USER="admin" \
    MYSQL_PASSWORD="admin" \
    MYSQL_ROOT_HOST=% \
    LANG=C.utf8mb4_unicode_ci

COPY [ "./sql/*.sql", "/docker-entrypoint-initdb.d/" ]

EXPOSE 3306
CMD ["--datadir", "/var/lib/mysql-no-volume", "--sql_mode", "STRICT_ALL_TABLES", "--character-set-server", "utf8mb4", "--collation-server", "utf8mb4_unicode_ci", "--lower_case_table_names=1"]