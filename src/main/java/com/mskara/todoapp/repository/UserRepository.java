package com.mskara.todoapp.repository;

import com.mskara.todoapp.entity.User;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsername(String username);
}
