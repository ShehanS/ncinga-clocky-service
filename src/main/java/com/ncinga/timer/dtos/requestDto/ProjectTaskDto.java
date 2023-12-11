package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskDto {
    private String id;
    private String email_before;
    private String description;
    private String title;
    private ActualEndTime actual_end_time;
    private ActualStartTime actual_start_time;
    private Owner owner;
    private Priority priority;
    private ScheduledEndTime scheduled_end_time;
    private Object task_type;
    private ScheduledStartTime scheduled_start_time;
    private Status status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActualEndTime {
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActualStartTime {
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {
        private String name;
        private String id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Priority {
        private String name;
        private String id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledEndTime {
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledStartTime {
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String name;
        private String id;
    }
}
