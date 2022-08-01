package com.mskara.todoapp.controller;

import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.model.entity.TodoItem;
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

    @GetMapping
    public ResponseEntity<List<TodoItem>> getTodoList() {
        return ResponseEntity.ok(todoService.getTodoList());
    }

    @PostMapping
    public ResponseEntity<TodoItem> addTodoItem(@Valid @RequestBody TodoItem todoItem) {
        return new ResponseEntity<>(todoService.addTodoItem(todoItem), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<TodoItem> updateTodoItemStatus(@PathVariable Integer itemId, @RequestBody Status status) {
        return new ResponseEntity<>(todoService.updateTodoItemStatus(itemId, status), HttpStatus.OK);
    }
}