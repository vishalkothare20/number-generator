package com.numbergenerator.app.models;

import com.sun.javafx.collections.ImmutableObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class Sequence {
    String id;
    Integer goal;
    Integer step;
    List<Integer> numbers;

    public Sequence(Integer goal, Integer step) {
        this.id = UUID.randomUUID().toString();
        this.goal = goal;
        this.step = step;
        numbers = new CopyOnWriteArrayList<>();
    }

    // Returns true if sequence is not completed
    // else false
    public synchronized boolean addNextNumber() {
        if(numbers.isEmpty()) {
            numbers.add(goal);
            return true;
        } else {
            int nextNumber = numbers.get(numbers.size() -1) - step;
            if(nextNumber == 0) {
                numbers.add(nextNumber);
                return false;
            } else if(nextNumber >= 0) {
                numbers.add(nextNumber);
                return true;
            }
        }
        return false;
    }

    public List<Integer> getSequeunceNumbers() {
        return new ArrayList<>(this.numbers);
    }
}
