FROM couchbase/server:latest AS stage_base
# FROM couchbase:latest AS stage_base
RUN chmod +x /entrypoint.sh
COPY init-cbserver.sh /opt/couchbase/init/