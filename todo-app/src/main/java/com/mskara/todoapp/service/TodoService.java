package com.mskara.todoapp.service;

import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.model.entity.TodoItem;

import java.util.List;

public interface TodoService {

    List<TodoItem> getTodoList();

    TodoItem addTodoItem(TodoItem todoItem);

    TodoItem updateTodoItemStatus(Integer todoId, Status status);
}