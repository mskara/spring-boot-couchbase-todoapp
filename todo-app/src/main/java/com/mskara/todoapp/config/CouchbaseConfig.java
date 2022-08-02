package com.mskara.todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.mskara.todoapp.repository"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${config.couchbase.host}")
    private String host;

    @Value("${config.couchbase.username}")
    private String username;

    @Value("${config.couchbase.password}")
    private String password;

    @Value("${config.couchbase.bucketName}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return host;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
}