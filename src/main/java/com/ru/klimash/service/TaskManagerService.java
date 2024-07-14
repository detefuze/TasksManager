package com.ru.klimash.service;

import com.ru.klimash.entity.Task;
import com.ru.klimash.exceptions.ExistingTaskException;
import com.ru.klimash.exceptions.NotExistingTaskException;
import jakarta.transaction.Transactional;

public interface TaskManagerService {

    // Метод создания задачи
    @Transactional
    void create_task(Task my_task) throws ExistingTaskException;

    void delete_task(String my_title) throws NotExistingTaskException;
}
