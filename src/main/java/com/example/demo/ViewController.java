package com.example.demo;
import com.example.demo.model.FormState;
import com.example.demo.model.QueryResultSlice;
import com.example.demo.model.Semantic_search;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class ViewController {

    Semantic_search semantic_search = new Semantic_search();

    @GetMapping("/home")
    public String viewForm(Model model) {
        model.addAttribute("formstate", new FormState());
        return "form";
    }

    @PostMapping("/index")
    public String viewForm(@RequestParam Integer useraction, @RequestParam String args, Model model) {
        ArrayList<QueryResultSlice> output = null;
        try {
            output = semantic_search.query(useraction, args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("query_result", output);
        return "form";
    }
}