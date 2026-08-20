package com.mahi.aitaskmanager.dto;

import com.mahi.aitaskmanager.enums.SubtaskStatus;
import jakarta.validation.constraints.NotNull;

public class SubtaskStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private SubtaskStatus status;

    public SubtaskStatusUpdateRequest() {
    }

    public SubtaskStatus getStatus() {
        return status;
    }

    public void setStatus(SubtaskStatus status) {
        this.status = status;
    }
}
