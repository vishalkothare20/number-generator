package com.numbergenerator.app.models;

import com.numbergenerator.app.exceptions.InputNotValidException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Task {
    final String id;
    TaskStatus status;
    final List<Sequence> sequences;

    public Task(Integer goal, Integer step) {
        this(Arrays.asList(goal), Arrays.asList(step));
    }

    public Task(List<Integer> goals, List<Integer> steps) {
        if(goals == null || steps == null || goals.isEmpty() || steps.isEmpty() || goals.size()!=steps.size()) {
            throw new InputNotValidException("Input goals and steps not valid");
        }
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.status = TaskStatus.IN_PROGRESS;
        this.sequences = new CopyOnWriteArrayList<>();
        initSequence(goals, steps);
    }

    private void initSequence(List<Integer> goals, List<Integer> steps) {
        for(int i=0;i<goals.size(); i++) {
            Sequence sequence = new Sequence(goals.get(i), steps.get(i));
            sequence.addNextNumber();
            this.sequences.add(sequence);
        }
    }

    private void setCompleteStatus() {
        if(this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Task can't be set to success when it is not in progress already");
        }
        this.status = TaskStatus.SUCCESS;
    }

    public boolean isComplete() {
        return (TaskStatus.SUCCESS.equals(this.status) || TaskStatus.ERROR.equals(this.status));
    }

    public void setErrorStatus() {
        this.status = TaskStatus.ERROR;
    }

    public void nextStep() {
        if(this.status != TaskStatus.IN_PROGRESS) {
            return;
        }

        boolean numberAdded = false;
        for(Sequence sequence: this.sequences) {
            numberAdded = numberAdded | sequence.addNextNumber();
        }
        if(!numberAdded) {
            setCompleteStatus();
        }
    }

}
