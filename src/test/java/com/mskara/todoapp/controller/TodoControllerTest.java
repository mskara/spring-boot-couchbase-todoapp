package com.mskara.todoapp.controller;

import com.mskara.todoapp.exception.ItemNotFoundException;
import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.model.entity.TodoItem;
import com.mskara.todoapp.model.enums.Status;
import com.mskara.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest extends AbstractControllerTest {

    @MockBean
    private TodoService todoService;

    @Test
    void whenRequestWithoutToken_thenReturns403() throws Exception {

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isForbidden()); //403

        mockMvc.perform(post("/api/todo"))
                .andExpect(status().isForbidden()); //403

        mockMvc.perform(patch("/api/todo"))
                .andExpect(status().isForbidden()); //403
    }

    @Test
    void whenRequestWithDeletedUserToken_thenReturns404() throws Exception {

        when(todoService.getTodoList()).thenThrow(new UserNotFoundException("skara"));

        mockMvc.perform(get("/api/todo")
                        .header("Authorization", "Bearer " + "jwt_token"))
                .andExpect(status().isNotFound()) //404
                .andExpect(jsonPath("$.message").value("skara is not found!"));

    }

    @Test
    void whenGetTodoListWithToken_thenReturns200() throws Exception {

        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(new TodoItem(1, Status.TODO, "develop a new feature for app"));
        todoItemList.add(new TodoItem(2, Status.TODO, "buy a helmet for cycling"));
        todoItemList.add(new TodoItem(3, Status.TODO, "analyze the survey results"));

        when(todoService.getTodoList()).thenReturn(todoItemList);

        mockMvc.perform(get("/api/todo")
                        .header("Authorization", "Bearer " + "jwt_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].status").value("TODO"))
                .andExpect(jsonPath("$.[0].description").value("develop a new feature for app"));

        verify(todoService).getTodoList();

    }

    @Test
    void whenAddTodoItem_thenReturns201() throws Exception {

        TodoItem todoItem = new TodoItem("study english");

        mockMvc.perform(post("/api/todo")
                        .header("Authorization", "Bearer " + "jwt_token")
                        .contentType("application/json")
                        .content(json(todoItem)))
                .andExpect(status().isCreated());//201

        verify(todoService).addTodoItem(todoItem);

    }

    @Test
    void whenAddTodoItemWithInvalidField_thenReturns400() throws Exception {

        TodoItem todoItem = new TodoItem();

        mockMvc.perform(post("/api/todo")
                        .header("Authorization", "Bearer " + "jwt_token")
                        .contentType("application/json")
                        .content(json(todoItem)))
                .andExpect(status().isBadRequest()) //400
                .andExpect(jsonPath("$.message").value("Invalid Parameters!"))
                .andExpect(jsonPath("$.path").value("/api/todo"))
                .andExpect(jsonPath("$.details[0]").value("description : must not be empty"));

    }

    @Test
    void whenUpdateTodoItemWithValidInfo_thenReturns200() throws Exception {

        mockMvc.perform(patch("/api/todo/{itemId}", "1")
                        .header("Authorization", "Bearer " + "jwt_token")
                        .contentType("application/json")
                        .content(json(Status.DONE)))
                .andExpect(status().isOk()); //200

        verify(todoService).updateTodoItemStatus(1, Status.DONE);
    }

    @Test
    void whenUpdateTodoItemWithIncorrectItemId_thenReturns404() throws Exception {

        when(todoService.updateTodoItemStatus(1, Status.DONE)).thenThrow(new ItemNotFoundException(1));

        mockMvc.perform(patch("/api/todo/{itemId}", "1")
                        .header("Authorization", "Bearer " + "jwt_token")
                        .contentType("application/json")
                        .content(json(Status.DONE)))
                .andExpect(status().isNotFound())//404
                .andExpect(jsonPath("$.message").value("Item is not found with this id: 1"))
                .andExpect(jsonPath("$.path").value("/api/todo/1"));

    }


}