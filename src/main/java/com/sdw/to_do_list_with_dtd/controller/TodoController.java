package com.sdw.to_do_list_with_dtd.controller;

import com.sdw.to_do_list_with_dtd.dto.TodoDto;
import com.sdw.to_do_list_with_dtd.model.Todo;
import com.sdw.to_do_list_with_dtd.model.User;
import com.sdw.to_do_list_with_dtd.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoRepository todoRepository;

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping
    public String list(HttpSession httpSession, Model model) {
        User user = getCurrentUser(httpSession);

        if (user == null) {
            return "redirect:/login";
        }

        List<Todo> list = todoRepository.findAllByUserId(user.getId());
        model.addAttribute("todos", list);

        return "todo-list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession httpSession, Model model) {
        if (getCurrentUser(httpSession) == null) return "redirect:/login";

        model.addAttribute("todoDto", new TodoDto());

        return "todo-form";
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute TodoDto todoDto,
            BindingResult bindingResult,
            HttpSession httpSession
    ) {
        if (bindingResult.hasErrors()) return "todo-form";

        User user = getCurrentUser(httpSession);

        Todo todo = Todo.builder()
                .userId(user.getId())
                .title(todoDto.getTitle())
                .completed(todoDto.isCompleted())
                .build();

        todoRepository.save(todo);

        return "redirect:/todos";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable int id,
            HttpSession httpSession
    ) {
        User user = getCurrentUser(httpSession);
        todoRepository.deleteByIdAndUserId(id, user.getId());

        return "redirect:/todos";
    }
}