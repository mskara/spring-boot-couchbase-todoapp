package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.exception.ItemNotFoundException;
import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.model.entity.TodoItem;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.repository.UserRepository;
import com.mskara.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final UserRepository userRepository;

    @Override
    public List<TodoItem> getTodoList() {
        return getCurrentUser().getTodoItemList();
    }

    @Override
    public TodoItem addTodoItem(TodoItem todoItem) {
        final User user = getCurrentUser();
        todoItem.setStatus(Status.TODO);

        if (Objects.isNull(user.getTodoItemList())) {
            final List<TodoItem> todoItemList = new ArrayList<>();
            todoItem.setId(1);
            todoItemList.add(todoItem);
            user.setTodoItemList(todoItemList);
        } else {
            todoItem.setId(user.getTodoItemList().size() + 1);
            user.getTodoItemList().add(todoItem);
        }

        userRepository.save(user);

        return todoItem;
    }

    @Override
    public TodoItem updateTodoItemStatus(Integer todoId, Status status) {
        final User user = getCurrentUser();
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

    private User getCurrentUser() {
        String loggedInUsername = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UserNotFoundException(loggedInUsername));
    }

}
