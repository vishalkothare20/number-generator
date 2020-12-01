package com.numbergenerator.app.resources;

import com.numbergenerator.app.exceptions.BaseException;
import com.numbergenerator.app.exceptions.InputNotValidException;
import com.numbergenerator.app.models.Sequence;
import com.numbergenerator.app.models.Task;
import com.numbergenerator.app.requests.TaskRequest;
import com.numbergenerator.app.responses.TaskResponse;
import com.numbergenerator.app.responses.TaskResult;
import com.numbergenerator.app.responses.TaskResults;
import com.numbergenerator.app.services.TaskService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Path("/api/")
public class TaskResource {
    TaskService taskService;
    static String NUM_LIST_ACTION = "get_numlist";

    @GET
    @Path("/hello")
    public String hello() {
        return "HELLO";
    }

    @POST
    @Path("/generate")
    public Response createTask(TaskRequest taskRequest) {
        if(taskRequest.getGoal()== null || taskRequest.getStep() == null) {
            throw new InputNotValidException("Input goal and step not valid");
        }
        Task task = taskService.addTask(taskRequest.getGoal(), taskRequest.getStep());
        TaskResponse taskResponse = new TaskResponse(task.getId());
        return Response.status(Response.Status.ACCEPTED).entity(taskResponse).build();
    }

    @GET
    @Path("/tasks/{task_id}/status")
    public Response getTaskStatus(@PathParam("task_id") @NonNull String taskId) {
        Task task = taskService.getTaskById(taskId);
        TaskResult taskResult = new TaskResult(task.getStatus().name());
        return Response.status(Response.Status.OK).entity(taskResult).build();
    }

    @GET
    @Path("/tasks/{task_id}")
    public Response get(@PathParam("task_id") @NonNull String taskId,
                                  @QueryParam("action") @NonNull String actionId) {
        if(!actionId.equals(NUM_LIST_ACTION)) {
            throw new BaseException("Action not supported");
        }
        Task task = taskService.getTaskById(taskId);
        List<Sequence> sequences = task.getSequences();
        // Handling both cases
        // 1. Single sequence in task
        // 2. Multiple sequence in task (Advance case)
        if(sequences.size() == 1) {
            String result = sequences.get(0).getNumbers().stream()
                    .map(String::valueOf).collect(Collectors.joining(","));
            return Response.status(Response.Status.OK).entity(new TaskResult(result)).build();
        } else {
            List<String> results = new ArrayList<>();
            for(Sequence sequence: sequences) {
                String result = sequence.getNumbers().stream()
                        .map(String::valueOf).collect(Collectors.joining(","));
                results.add(result);
            }
            return Response.status(Response.Status.OK).entity(new TaskResults(results)).build();
        }
    }

    @POST
    @Path("/bulkGenerate")
    public Response bulkGenerate(@NotNull List<TaskRequest> taskRequests) {
        List<Integer> goals = taskRequests.stream()
                .map(taskRequest -> taskRequest.getGoal())
                .collect(Collectors.toList());
        List<Integer> steps = taskRequests.stream()
                .map(taskRequest -> taskRequest.getStep())
                .collect(Collectors.toList());
        Task task = taskService.addTask(goals, steps);
        TaskResponse taskResponse = new TaskResponse(task.getId());
        return Response.status(Response.Status.ACCEPTED).entity(taskResponse).build();
    }




}
