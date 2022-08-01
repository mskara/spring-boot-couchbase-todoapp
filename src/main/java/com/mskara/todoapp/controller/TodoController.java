package com.mskara.todoapp.controller;

import com.mskara.todoapp.entity.TodoItem;
import com.mskara.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todo")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{username}")
    public ResponseEntity<List<TodoItem>> findTodoListOfUser(@PathVariable String username) {
        return ResponseEntity.ok(todoService.getTodoListByUser(username));
    }

    @PostMapping("/{username}")
    public ResponseEntity<TodoItem> put(@Valid @RequestBody TodoItem todoItem, @PathVariable String username) {
        return new ResponseEntity<>(todoService.addTodoItemByUser(username, todoItem), HttpStatus.CREATED);
    }

    @PatchMapping("/{username}/{todoId}")
    public ResponseEntity<TodoItem> done(@PathVariable String username, @PathVariable Integer todoId, @RequestBody boolean completed) {
        return new ResponseEntity<>(todoService.updateTodoItemStatus(username, todoId, completed), HttpStatus.OK);
    }
}