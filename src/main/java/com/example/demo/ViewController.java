package com.example.demo;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final List<User> users = new ArrayList<>();

    @GetMapping("/home")
    public String helloView(Model model) {
        model.addAttribute("message", "Hello from Spring MVC + Thymeleaf!");
        model.addAttribute("userlist", users);
        return "hello";
    }

    @GetMapping("/parameter")
    public String helloViewName(@RequestParam(name = "name", required = false, defaultValue = "Guest") String name, Model model) {
        model.addAttribute("message", "Hello " + name + ", from Spring MVC + Thymeleaf!");
        model.addAttribute("userlist", users);
        return "hello";
    }

    @GetMapping("/form")
    public String viewForm(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping("/parameter")
    public String handleForm(@ModelAttribute("user") User user, Model model) {
        users.add(user);
        model.addAttribute("message", "Hello " + user.name + ", from Spring MVC + Thymeleaf!");
        model.addAttribute("userlist", users);
        return "hello";
    }
}