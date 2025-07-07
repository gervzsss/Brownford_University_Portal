package com.brownford.service;

import com.brownford.model.ActivityLog;
import com.brownford.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void log(String username, String action, String details) {
        ActivityLog log = new ActivityLog(username, action, details, LocalDateTime.now());
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getRecentLogs(int count) {
        return activityLogRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, count)).getContent();
    }

    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, Integer.MAX_VALUE)).getContent();
    }
}
