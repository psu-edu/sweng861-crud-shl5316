package com.sweng861.agiletracker.repository;

import com.sweng861.agiletracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRespository extends JpaRepository<Task, Long> {
}
