package com.mskara.todoapp.service;

import com.mskara.todoapp.entity.TodoItem;

import java.util.List;

public interface TodoService {

    List<TodoItem> getTodoListByUser(String username);

    TodoItem addTodoItemByUser(String username, TodoItem todoItem);

    TodoItem updateTodoItemStatus(String username, Integer todoId, boolean completed);

}
