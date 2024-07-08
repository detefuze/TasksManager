package com.ru.klimash;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main")
public class ListManagerController {

    @GetMapping("")
    public String showButtons() {
        return "main";
    }

    @GetMapping("/create_tasks")
    public String createTasks() {
        return "create_tasks";
    }

    @GetMapping("/check_tasks")
    public String checkTasks(Model model) {

        return "check_tasks";
    }

    @PostMapping("create_task/new_task")
    public String newTask(@RequestParam(required = false) String title,
                          @RequestParam String task_text)
    {
        return "redirect:/main/create_tasks";
    }
}