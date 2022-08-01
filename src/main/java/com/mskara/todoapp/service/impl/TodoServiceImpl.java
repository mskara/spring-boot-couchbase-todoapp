package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.model.entity.TodoItem;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.exception.ItemNotFoundException;
import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.repository.UserRepository;
import com.mskara.todoapp.service.TodoService;
import com.mskara.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<TodoItem> getTodoList() {
        return userService.getCurrentUser().getTodoItemList();
    }

    @Override
    public TodoItem addTodoItem(TodoItem todoItem) {
        final User user = userService.getCurrentUser();

        if (Objects.isNull(user.getTodoItemList())) {
            final List<TodoItem> todoItemList = new ArrayList<>();
            todoItemList.add(todoItem);
            user.setTodoItemList(todoItemList);
        } else {
            user.getTodoItemList().add(todoItem);
        }

        userRepository.save(user);

        return todoItem;
    }

    @Override
    public TodoItem updateTodoItemStatus(Integer todoId, Status status) {
        final User user = userService.getCurrentUser();
        final List<TodoItem> todoItemList = user.getTodoItemList();
        final TodoItem todoItem = todoItemList
                .stream()
                .filter(i -> i.getId().equals(todoId))
                .findAny()
                .orElseThrow(() -> new ItemNotFoundException(todoId));

        todoItem.setStatus(status);
        userRepository.save(user);
        return todoItem;
    }


}
