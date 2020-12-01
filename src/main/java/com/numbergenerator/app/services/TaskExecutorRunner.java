package com.numbergenerator.app.services;

import com.numbergenerator.app.config.TaskConfiguration;
import com.numbergenerator.app.models.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class TaskExecutorRunner implements CommandLineRunner {
    TaskService taskService;
    TaskConfiguration taskConfiguration;

    @Override
    public void run(String... args) throws Exception {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::execute, 1, taskConfiguration.getDelayInSeconds(),
                TimeUnit.SECONDS);
    }

    private void execute() {
        for(Task task: taskService.getAllTasks()) {
            if(!task.isComplete()) {
                task.nextStep();
            }
        }
    }
}
