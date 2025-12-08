package com.example.demo.model;

import com.example.demo.Struct;

import java.util.ArrayList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.lang.Object;

import static java.lang.Math.pow;

/*
all-MiniLM-L6-v2
* */

public class Semantic_search {
    String pythonPath = "C:\\Users\\visvesh\\txtaienv\\Scripts\\python.exe";
    String scriptPath = "C:\\Users\\visvesh\\programming\\java_IntelliJ\\txtai_semantic_search\\main.py";

    ProcessBuilder pb = new ProcessBuilder();
    Process process = null;

    ArrayList<String> data = new ArrayList<String>();
    Boolean constructed = false;

    BufferedReader reader;
    PrintWriter writer;

    public Semantic_search() {
        pb = new ProcessBuilder(pythonPath, scriptPath);
        pb.redirectErrorStream(true);
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer = new PrintWriter(
                new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8),
                true   // autoFlush
        );
        writer.println("setup");
        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line;
        int nline;
        try {
            if((line = reader.readLine()) != null)
                nline = Integer.parseInt(line);
            else
                nline = -1;
            while ((line = reader.readLine()) != null) {
                data.add(line);
                nline = nline - 1;
                if(nline == 0)
                    break;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(data.size());
        constructed = true;
    }

    //if qurey string size = 0 then setup embeddings , if = 1 then run
    public ArrayList<Struct<String, String, Float, Boolean>> query(String query_string, Integer querytype) throws IOException, InterruptedException {
        while (!constructed) {wait();}
        String[] arr = {"single", "batch", "batchmore"};
        writer.println("query "+arr[querytype-1]);
        if(querytype!=3)
            writer.println(query_string);
        ArrayList<Struct<String, String, Float, Boolean>> best_matches = new ArrayList<Struct<String, String, Float, Boolean>>();
        // Print output

        String line;
        int nline=-1;
        if((line = reader.readLine()) != null)
            nline = Integer.parseInt(line);
        else
            nline = -1;
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String text = data.get(Integer.parseInt(tokens[0]));
                Float score = Float.parseFloat(tokens[1]);
                String[] temp = text.split(" ", 2);
                Boolean reciprocal =  (tokens[2].equals("1")? true:false);
                best_matches.add(new Struct<String, String, Float, Boolean>(temp[0], temp[1], (float)Math.pow(score,1), reciprocal));
                nline = nline - 1;
                if(nline == 0)
                    break;
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return best_matches;
    }
}