package com.mskara.todoapp.controller;

import com.mskara.todoapp.config.SwaggerConfig;
import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.model.entity.TodoItem;
import com.mskara.todoapp.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {SwaggerConfig.TODO_TAG})
@RestController
@RequestMapping("api/todo")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @ApiOperation(value = "Returns todo list of user")
    @GetMapping
    public ResponseEntity<List<TodoItem>> getTodoList() {
        return ResponseEntity.ok(todoService.getTodoList());
    }

    @ApiOperation(value = "Add new todo item to todo list of user")
    @PostMapping
    public ResponseEntity<TodoItem> addTodoItem(@Valid @RequestBody TodoItem todoItem) {
        return new ResponseEntity<>(todoService.addTodoItem(todoItem), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update status of todo item with given id")
    @PatchMapping("/{itemId}")
    public ResponseEntity<TodoItem> updateTodoItemStatus(@PathVariable Integer itemId, @RequestBody Status status) {
        return new ResponseEntity<>(todoService.updateTodoItemStatus(itemId, status), HttpStatus.OK);
    }
}