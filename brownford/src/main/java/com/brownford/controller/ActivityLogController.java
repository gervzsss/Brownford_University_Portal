package com.brownford.controller;

import com.brownford.model.ActivityLog;
import com.brownford.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogService activityLogService;

    // Get the most recent N logs (default 5)
    @GetMapping("/recent")
    public List<ActivityLog> getRecentLogs(@RequestParam(defaultValue = "5") int count) {
        return activityLogService.getRecentLogs(count);
    }

    // Get all logs (paginated in future if needed)
    @GetMapping
    public List<ActivityLog> getAllLogs() {
        return activityLogService.getAllLogs();
    }

    // Get all logs (paginated in future if needed)
    @GetMapping("/all")
    public List<ActivityLog> getAllLogsAlias() {
        return activityLogService.getAllLogs();
    }
}
