package com.example.demo;
import com.example.demo.model.FormState;
import com.example.demo.model.QueryResultSlice;
import com.example.demo.model.Semantic_search;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class ViewController {

    Semantic_search semantic_search = new Semantic_search();

    @GetMapping("/index")
    public String viewForm(Model model) {
        model.addAttribute("formstate", new FormState());
        ArrayList<QueryResultSlice> output = null;
        try {
            output = semantic_search.query(9, "3 10");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("query_result", output);
        return "form";
    }

    @PostMapping("/index")
    public String viewForm(@RequestParam Integer useraction, @RequestParam String args, Model model) {
        ArrayList<QueryResultSlice> output = null;
        ArrayList<QueryResultSlice> privateNotesTags = null;
        try {
            output = semantic_search.query(useraction, args);
            privateNotesTags = semantic_search.GetPrivateNotesTags();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("query_result", output);
        model.addAttribute("privatenodetags", privateNotesTags);
        model.addAttribute("new_entries", semantic_search.newEntries);
        return "form";
    }
}