package com.example.demo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Semantic_search;
import com.example.demo.Struct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ViewController {

    private final List<User> users = new ArrayList<>();
    Semantic_search semantic_search = new Semantic_search();

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
    public String viewForm(@RequestParam(name = "name", required = false, defaultValue = "user") String name, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("name", name);
        model.addAttribute("formdetails", new FormDetails());
        return "form";
    }

    @PostMapping("/parameter")
    public String handleForm(@ModelAttribute("user") User user, Model model) {
        users.add(user);
        model.addAttribute("message", "Hello " + user.name + ", from Spring MVC + Thymeleaf!");
        model.addAttribute("userlist", users);
        return "hello";
    }

    @PostMapping("/instagram")
    public String viewForm(@ModelAttribute("user") User user, @RequestParam("form_state") Integer formstate, Model model) {
        ArrayList<Struct<String, String, Float, Boolean>> output = null;
        ArrayList<Struct<String, String, Float, Boolean>> URLs = new ArrayList<Struct<String, String, Float, Boolean>>();
        FormDetails formDetails_temp = new FormDetails();
        formDetails_temp.from_inputbar = (formstate == 1);
        Integer sum = 0;
        try {
            output = semantic_search.query(user.name, formstate);
            for(int i = 0; i < output.size(); i++) {
                URLs.add(new Struct<String, String, Float, Boolean>(output.get(i).key, output.get(i).value, output.get(i).score, (!formDetails_temp.from_inputbar)&&output.get(i).reciprocal));
                sum+=(output.get(i).reciprocal?0:1);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        formDetails_temp.showmore=(formstate == 2) && (sum*5<output.size());
        model.addAttribute("formdetails", formDetails_temp);
        model.addAttribute("message", Integer.toString(output.size()));
        model.addAttribute("name", "insta");
        model.addAttribute("query_result", URLs);
        return "form";
    }
}