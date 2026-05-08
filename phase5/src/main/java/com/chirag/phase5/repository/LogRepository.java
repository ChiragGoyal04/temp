package com.chirag.phase5.repository;

import com.chirag.phase5.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntry,Long> {
}
