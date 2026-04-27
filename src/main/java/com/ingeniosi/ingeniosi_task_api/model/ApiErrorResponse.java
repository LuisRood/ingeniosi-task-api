package com.ingeniosi.ingeniosi_task_api.model;

import java.util.List;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
}
