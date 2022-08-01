package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.entity.TodoItem;
import com.mskara.todoapp.entity.User;
import com.mskara.todoapp.exception.ItemNotFoundException;
import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.repository.UserRepository;
import com.mskara.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final UserRepository userRepository;

    @Override
    public List<TodoItem> getTodoListByUser(String username) {
        return getUserByUsername(username).getTodoItemList();
    }

    @Override
    public TodoItem addTodoItemByUser(String username, TodoItem todoItem) {
        final User user = getUserByUsername(username);

        if (Objects.isNull(user.getTodoItemList())) {
            List<TodoItem> todoItemList = new ArrayList<>();
            todoItemList.add(todoItem);
            user.setTodoItemList(todoItemList);
        } else {
            user.getTodoItemList().add(todoItem);
        }

        userRepository.save(user);

        return todoItem;
    }

    @Override
    public TodoItem updateTodoItemStatus(String username, Integer todoId, boolean completed) {
        final User user = getUserByUsername(username);
        final List<TodoItem> todoItemList = user.getTodoItemList();
        final TodoItem todoItem = todoItemList
                .stream()
                .filter(i -> i.getId().equals(todoId))
                .findAny()
                .orElseThrow(() -> new ItemNotFoundException(todoId));

        todoItem.setCompleted(completed);
        userRepository.save(user);
        return todoItem;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
