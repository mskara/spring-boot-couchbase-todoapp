package com.mskara.todoapp.repository;

import com.mskara.todoapp.model.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotEmpty String username);

}