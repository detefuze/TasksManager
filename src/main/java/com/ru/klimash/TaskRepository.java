package com.ru.klimash;

import com.ru.klimash.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    Optional<Task> findByTitleAndText(String title, String text);
}
