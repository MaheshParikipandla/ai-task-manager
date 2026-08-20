package com.mahi.aitaskmanager.exception;

public class SubtaskNotFoundException extends RuntimeException {

    public SubtaskNotFoundException(Long taskId, Long subtaskId) {
        super("Subtask with id " + subtaskId + " not found for task " + taskId);
    }
}
