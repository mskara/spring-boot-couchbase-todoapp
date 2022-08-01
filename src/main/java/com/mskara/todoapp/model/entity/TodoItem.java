package com.mskara.todoapp.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mskara.todoapp.model.enums.Status;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TodoItem {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Status status;

    @NotEmpty
    private String description;

}
