package com.mskara.todoapp.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mskara.todoapp.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    public TodoItem(String description) {
        this.description = description;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Status status;

    @NotEmpty
    private String description;

}
