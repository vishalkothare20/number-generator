package com.numbergenerator.app;

import com.numbergenerator.app.models.Task;
import com.numbergenerator.app.requests.TaskRequest;
import com.numbergenerator.app.responses.TaskResponse;
import com.numbergenerator.app.responses.TaskResult;
import com.numbergenerator.app.responses.TaskResults;
import com.numbergenerator.app.services.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ResourceTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TaskService taskService;

    @Test
    public void testGenerateTask() {
        TaskRequest taskRequest = new TaskRequest(20, 2);
        ResponseEntity<TaskResponse> entity = this.restTemplate.postForEntity("/api/generate", taskRequest,
                TaskResponse.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(entity.getBody().getTask()).isNotBlank();
    }

    @Test
    public void testGetTaskStatus() {
        Task task = taskService.addTask(100, 2);
        String url = "/api/tasks/" + task.getId() +"/status";
        ResponseEntity<TaskResult> entity = this.restTemplate.getForEntity(url, TaskResult.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getResult()).contains("IN_PROGRESS");
    }

    @Test
    public void testGetNumbers() {
        Task task = taskService.addTask(100, 2);
        String url = "/api/tasks/" + task.getId() +"?action=get_numlist";
        ResponseEntity<TaskResult> entity = this.restTemplate.getForEntity(url, TaskResult.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getResult().split(",")).contains("100");
    }

    @Test
    public void testGenerateTaskBulk() {
        TaskRequest taskRequest1 = new TaskRequest(20, 2);
        TaskRequest taskRequest2 = new TaskRequest(100, 2);
        List<TaskRequest> requests = new ArrayList<TaskRequest>(){{ add(taskRequest1); add(taskRequest2);}};
        ResponseEntity<TaskResponse> entity = this.restTemplate.postForEntity("/api/bulkGenerate", requests,
                TaskResponse.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(entity.getBody().getTask()).isNotBlank();
    }

    @Test
    public void testGetNumbersBulk() {
        List<Integer> goals = Arrays.asList(100, 20);
        List<Integer> steps = Arrays.asList(10, 5);
        Task task = taskService.addTask(goals, steps);
        String url = "/api/tasks/" + task.getId() +"?action=get_numlist";
        ResponseEntity<TaskResults> entity = this.restTemplate.getForEntity(url, TaskResults.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getResults().stream().findFirst().get().split(",")).contains("100");
    }

}
