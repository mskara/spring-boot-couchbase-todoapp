package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.exception.ItemNotFoundException;
import com.mskara.todoapp.exception.UserAlreadyExistsException;
import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.model.entity.TodoItem;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private UserRepository userRepository;

    @BeforeAll
    public static void setup() {

        Authentication authentication = new UsernamePasswordAuthenticationToken("skara", null, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

    }

    @Test
    void whenGetTodoListOfCurrentUser_thenReturnList() {

        User user = User.builder()
                .name("serhat")
                .surname("kara")
                .username("skara")
                .build();


        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(new TodoItem(1, Status.TODO, "develop a new feature for app"));
        todoItemList.add(new TodoItem(2, Status.TODO, "buy a helmet for cycling"));
        todoItemList.add(new TodoItem(3, Status.TODO, "analyze the survey results"));

        user.setTodoItemList(todoItemList);

        when(userRepository.findByUsername("skara")).thenReturn(Optional.of(user));

        List<TodoItem> todoList = todoService.getTodoList();

        assertEquals(3, todoList.size());

    }

    @Test
    void whenGetTodoListOfCurrentUserAndUserIsNotInDb_thenThrowException() {

        when(userRepository.findByUsername("skara")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> todoService.getTodoList());

        assertEquals("skara is not found!", exception.getMessage());

    }

    @Test
    void whenUserHasNoTodoListAndAddNewOne_thenCreateTodoListAndAddThis() {

        User user = User.builder()
                .name("serhat")
                .surname("kara")
                .username("skara")
                .build();

        when(userRepository.findByUsername("skara")).thenReturn(Optional.of(user));

        TodoItem todoItem = todoService.addTodoItem(new TodoItem("study english"));

        assertEquals(1, user.getTodoItemList().size());
        assertEquals(1, todoItem.getId());
        assertEquals(Status.TODO, todoItem.getStatus());
    }

    @Test
    void whenUserHasTodoListAndAddNewOne_thenAddThisToExistingList() {

        User user = User.builder()
                .name("serhat")
                .surname("kara")
                .username("skara")
                .build();

        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(new TodoItem(1, Status.TODO, "develop a new feature for app"));
        todoItemList.add(new TodoItem(2, Status.TODO, "buy a helmet for cycling"));
        todoItemList.add(new TodoItem(3, Status.TODO, "analyze the survey results"));

        user.setTodoItemList(todoItemList);
        when(userRepository.findByUsername("skara")).thenReturn(Optional.of(user));

        TodoItem todoItem = todoService.addTodoItem(new TodoItem("study english"));

        assertEquals(4, user.getTodoItemList().size());
        assertEquals(4, todoItem.getId());
        assertEquals(Status.TODO, todoItem.getStatus());

    }

    @Test
    void whenMakeDoneTodoItemStatus_thenUpdateAndReturnThisItem() {

        User user = User.builder()
                .name("serhat")
                .surname("kara")
                .username("skara")
                .build();

        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(new TodoItem(1, Status.TODO, "develop a new feature for app"));
        todoItemList.add(new TodoItem(2, Status.TODO, "buy a helmet for cycling"));
        todoItemList.add(new TodoItem(3, Status.TODO, "analyze the survey results"));

        user.setTodoItemList(todoItemList);
        when(userRepository.findByUsername("skara")).thenReturn(Optional.of(user));

        TodoItem todoItem = todoService.updateTodoItemStatus(3, Status.DONE);

        assertEquals(3, todoItem.getId());
        assertEquals(Status.DONE, todoItem.getStatus());
        assertEquals("analyze the survey results", todoItem.getDescription());

    }

    @Test
    void whenUpdateTodoItemWithIncorrectId_thenThrowException() {

        User user = User.builder()
                .name("serhat")
                .surname("kara")
                .username("skara")
                .build();

        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(new TodoItem(1, Status.TODO, "develop a new feature for app"));

        user.setTodoItemList(todoItemList);
        when(userRepository.findByUsername("skara")).thenReturn(Optional.of(user));

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> todoService.updateTodoItemStatus(4, Status.DONE));

        assertEquals("Item is not found with this id: 4", exception.getMessage());
    }

}