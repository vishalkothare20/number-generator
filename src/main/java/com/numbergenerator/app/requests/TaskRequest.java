package com.numbergenerator.app.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NonNull
    Integer goal;
    @NonNull
    Integer step;
}
