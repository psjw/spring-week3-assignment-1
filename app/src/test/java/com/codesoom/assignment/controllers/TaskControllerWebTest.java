package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @BeforeEach
    void setUp(){
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"title\":\"Test Task\"}]"))
                .andExpect(content().json("[{\"id\":1,\"title\":\"Test Task\"}]"))
                .andExpect(content().string(containsString("Test Task")));
    }



}