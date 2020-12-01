package com.numbergenerator.app.services;

import com.numbergenerator.app.exceptions.TaskNotFoundException;
import com.numbergenerator.app.models.Task;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
    Map<String, Task> taskMap;

    public TaskService() {
        taskMap = new ConcurrentHashMap<>();
    }

    public Task addTask(Integer goal, Integer step) {
        Task task = new Task(goal, step);
        taskMap.put(task.getId(), task);
        return task;
    }

    public Task addTask(List<Integer> goals, List<Integer> steps) {
        Task task = new Task(goals, steps);
        taskMap.put(task.getId(), task);
        return task;
    }

    public Task getTaskById(String taskId) {
        if(!taskMap.containsKey(taskId)) {
            throw new TaskNotFoundException("No task exists with id: "+ taskId);
        }
        return taskMap.get(taskId);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }
}
