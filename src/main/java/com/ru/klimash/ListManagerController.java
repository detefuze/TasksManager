package com.ru.klimash;

import com.ru.klimash.entity.Task;
import com.ru.klimash.exceptions.ExistingTaskException;
import com.ru.klimash.exceptions.NotExistingTaskException;
import com.ru.klimash.service.HibernateRunner;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@RequestMapping("/main")
public class ListManagerController {

    private final static Logger logger_controller = LoggerFactory.getLogger(ListManagerController.class);

    private final static SessionFactory sessionFactory = new Configuration()
            .configure("application.properties")
            .buildSessionFactory();
    private final HibernateRunner runner = new HibernateRunner();

    public static SessionFactory getSessionFactory(){ return sessionFactory; }

    // Главная страница
    @GetMapping("")
    public String showMain() {
        logger_controller.info("ON MAIN PAGE");
        HibernateRunner.setSTATUS();
        return "main";
    }
    // Страница создания задачи
    @GetMapping("/create_tasks")
    public String createTasks(Model model) {
        logger_controller.info("ON CREATE PAGE");
        model.addAttribute("status", HibernateRunner.getSTATUS());
        return "create_tasks";
    }
    // Страница удаления задачи
    @GetMapping("/delete_tasks")
    public String deleteTasks(Model model) {
        logger_controller.info("ON DELETE PAGE");
        model.addAttribute("status", HibernateRunner.getSTATUS());
        return "delete_tasks";
    }
    // Создание задачи
    @PostMapping("create_task/new_task")
    public String newTask(@RequestParam String title,
                          @RequestParam String task_text) throws ExistingTaskException
    {
        runner.create_task(new Task(title, task_text));
        return "redirect:/main/create_tasks";
    }

    // Удаление задачи
    @PostMapping("delete_tasks/deletion")
    public String deleteTask(@RequestParam String title,
                             @RequestParam String task_text) throws NotExistingTaskException
    {
        runner.delete_task(new Task(title, task_text));
        return "redirect:/main/delete_tasks";
    }
}