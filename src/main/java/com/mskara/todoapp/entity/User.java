package com.mskara.todoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Document
@Data
public class User {

    @JsonIgnore
    @Id
    private String id;

    @Field
    @NotEmpty
    private String name;

    @Field
    private String surname;

    @Field
    @NotEmpty
    private String username;

    @Field
    @NotEmpty
    private String mail;

    @Field
    @JsonIgnore
    private List<TodoItem> todoItemList;
}