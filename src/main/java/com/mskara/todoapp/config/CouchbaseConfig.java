package com.mskara.todoapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.mskara.todoapp.repository"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {


    @Override
    public String getConnectionString() {
        return ("127.0.0.1");
    }

    @Override
    public String getUserName() {
        return "mskara";
    }

    @Override
    public String getPassword() {
        return "123456";
    }

    @Override
    public String getBucketName() {
        return "todoapp";
    }
}