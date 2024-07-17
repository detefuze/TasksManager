package com.ru.klimash;

import com.ru.klimash.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Stack;

@Controller
@Component
@RequestMapping("/main")
public class ListManagerController {

    private final static Logger logger_controller = LoggerFactory.getLogger(ListManagerController.class);

    @Autowired
    private TaskRepository taskRepository;

    private static String STATUS = "Waiting";

    public static void setSTATUS() {
        ListManagerController.STATUS = "Waiting";
    }

    public static String getSTATUS() {
        return STATUS;
    }

    // Главная страница
    @GetMapping("")
    public String showMain() {
        logger_controller.info("ON MAIN PAGE");
        ListManagerController.setSTATUS();
        return "main";
    }
    // Страница создания задачи
    @GetMapping("/create_tasks")
    public String createTasks(Model model) {
        logger_controller.info("ON CREATE PAGE");
        model.addAttribute("status", ListManagerController.getSTATUS());
        return "create_tasks";
    }
    // Страница удаления задачи
    @GetMapping("/delete_tasks")
    public String deleteTasks(Model model) {
        logger_controller.info("ON DELETE PAGE");
        model.addAttribute("status", ListManagerController.getSTATUS());
        return "delete_tasks";
    }
    // Создание задачи
    @PostMapping("create_task/new_task")
    public String newTask(@RequestParam String title,
                          @RequestParam String text)
    {
        if (taskRepository.findByTitleAndText(title, text).isPresent())
        {
            STATUS = "Rejected";
            logger_controller.error("You are adding an existing task!");
        } else {
            taskRepository.save(new Task(title, text));
            STATUS = "Submitted";
            logger_controller.info("You added a new task!");
        }
        return "redirect:/main/create_tasks";
    }

    // Удаление задачи
    @PostMapping("delete_tasks/deletion")
    public String deleteTask(@RequestParam Integer id)
    {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent())
        {
            taskRepository.deleteById(id);
            STATUS = "Submitted";
            logger_controller.error("You deleted the task!");
        } else {
            STATUS = "Rejected";
            logger_controller.error("You are trying to delete an non-existing task!");
        }
        return "redirect:/main/delete_tasks";
    }
}