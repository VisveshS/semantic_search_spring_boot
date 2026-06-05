package com.example.demo.model;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/*
all-MiniLM-L6-v2
* */

public class Semantic_search {
    String pythonPath = "C:\\Users\\visvesh\\txtaienv\\Scripts\\python.exe";
    String scriptPath = "C:\\Users\\visvesh\\programming\\java_IntelliJ\\txtai_semantic_search\\main.py";

    ProcessBuilder pb = new ProcessBuilder();
    Process process = null;

    Boolean constructed = false;
    ArrayList<QueryResultSlice> queryoutput = new ArrayList<>();
    ArrayList<QueryResultSlice> privateNotesTags = new ArrayList<>();
    public ArrayList<String> newEntries = new ArrayList<String>();
    public ArrayList<String> persist = new ArrayList<String>(); //url from bulk insertion

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
        try {
            String line = reader.readLine();
            System.out.println(line + " embeddings read");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        constructed = true;
        for(int i=0;i<10;i++)
            persist.add("");
    }

    //if qurey string size = 0 then setup embeddings , if = 1 then run
//    output[0] = (int id, string url, string description, int in_knn_length, float pagerank, float clustering score, boolean cansave, string website, num_embeddings, url_bulk_insert) [details of_node]
//    output[1:] = (int id, string url, string description, int in_knn_length, float pagerank, mutual knn, website)
    public ArrayList<QueryResultSlice> query(Integer action, String args) throws IOException, InterruptedException {
        while (!constructed) {wait();}
        String line;
        String dburl;
        String dbdesc;
        String pagerank;
        int nline=-1;
        if(action == 0) {
            writer.println("querynode");
            writer.println(args);
            try {
                line = reader.readLine();
                if(line.compareTo("0") == 0)
                    return queryoutput;
                dburl = reader.readLine();
                dbdesc = reader.readLine();
                String nodeid_in_knn_count = reader.readLine();
                pagerank = reader.readLine();
                String mutual_knn = reader.readLine();
                String clustering = reader.readLine();
                String cansave = reader.readLine();
                String num_embeddings = reader.readLine();
                queryoutput.clear();
                queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of(args, dburl, dbdesc,nodeid_in_knn_count, pagerank, clustering, cansave, QueryResultSlice.fetchSourceSite(dburl), num_embeddings, persist.get(0)))));
                line = reader.readLine();
                nline = Integer.parseInt(line);
                for(int i=0;i<nline;i++) {
                    dburl = reader.readLine();
                    dbdesc = reader.readLine();
                    line = reader.readLine();
                    String[] tokens = line.split(" ");
                    queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of(tokens[0], dburl, dbdesc, tokens[1],tokens[2],String.valueOf(mutual_knn.charAt(i)),QueryResultSlice.fetchSourceSite(dburl)))));
                }
                line = reader.readLine();
                nline = Integer.parseInt(line);
                privateNotesTags.clear();
                System.out.println(line);
                for(int i=0;i<nline;i++) {
                    String label = reader.readLine();
                    String score = reader.readLine();
                    System.out.println(label);
                    privateNotesTags.add(new QueryResultSlice(new ArrayList<>(List.of(label, score))));
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        else if(action == 1) {
            writer.println("querystring");
            writer.println(args);
            queryoutput.clear();
            try {
                String cscore = reader.readLine();
                String mutual_knn = reader.readLine();
                String cansave = reader.readLine();
                String num_embeddings = reader.readLine();
                line = reader.readLine();
                nline = Integer.parseInt(line);
                queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of("", "", args,"","", cscore, cansave,"", num_embeddings, persist.get(0)))));
                for(int i=0;i<nline;i++) {
                    dburl = reader.readLine();
                    dbdesc = reader.readLine();
                    line = reader.readLine();
                    String[] tokens = line.split(" ");
                    Integer nid = Integer.parseInt(tokens[0]);
                    queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of(tokens[0], dburl, dbdesc,tokens[1],tokens[2],String.valueOf(mutual_knn.charAt(i)),QueryResultSlice.fetchSourceSite(dburl)))));
                }
                line = reader.readLine();
                nline = Integer.parseInt(line);
                privateNotesTags.clear();
                for(int i=0;i<nline;i++) {
                    String label = reader.readLine();
                    String score = reader.readLine();
                    privateNotesTags.add(new QueryResultSlice(new ArrayList<>(List.of(label, score))));
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
//always follows query string by design
        else if (action == 2) {
            writer.println("insert");
            writer.println(args);
            line = reader.readLine();
            Integer new_index = Integer.parseInt(line);
            String nodeid_in_knn_count = reader.readLine();
            pagerank = reader.readLine();
            String mutual_knn = reader.readLine();
            String cscore = reader.readLine();
            String cansave = reader.readLine();
            String insertfield[] = args.split(" ",2);
            String num_embeddings = String.valueOf(Integer.parseInt(queryoutput.get(0).fetchSlice().get(8))+1);
            queryoutput.set(0,new QueryResultSlice(new ArrayList<>(List.of(new_index.toString(), insertfield[0], insertfield[1], nodeid_in_knn_count,pagerank,cscore,cansave,QueryResultSlice.fetchSourceSite(insertfield[0]), num_embeddings, persist.get(0)))));
            for(int i=1;i<queryoutput.size();i++) {
                queryoutput.get(i).fetchSlice().set(5,String.valueOf(mutual_knn.charAt(i-1)));
            }
            persist.set(0,"");
        }
//    output[0] = (int id, string url, string description, int in_knn_length, float pagerank, float triangle_mesh_fraction, cansave) [details of_node]
//    output[1:] = (int id, string url, string description, int in_knn_length, float pagerank, mutual knn)
//always follows query node by design
        else if (action == 3) {
            writer.println("delete");
            writer.println(args);
            String clustering = reader.readLine();
            line = reader.readLine();
            queryoutput.get(0).fetchSlice().set(5,clustering);
            queryoutput.get(0).fetchSlice().set(0,"");
            Integer num_embeddings = Integer.parseInt(queryoutput.get(0).fetchSlice().get(8));
            queryoutput.get(0).fetchSlice().set(8,String.valueOf(num_embeddings-1));
//            for(int i=1;i<queryoutput.size();i++) {
//                queryoutput.get(i).fetchSlice().set(5,"0");
//            }
        }
        else if (action == 4) {
            writer.println("save");
            writer.println("browse");
            line = reader.readLine();
        }
        else if (action == 5) {
            queryoutput.clear();
            writer.println("save");
            writer.println("exit");
            new Thread(() -> {
                try {
                    Thread.sleep(200); // small delay so response is flushed
                    System.exit(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        else if (action == 6) {
            writer.println("exit");
            new Thread(() -> {
                queryoutput.clear();
                try {
                    Thread.sleep(100); // small delay so response is flushed
                    System.exit(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        //bulk input, no processing, convienience only
        else if (action == 7) {
            newEntries.clear();
            if(args.length()>0)
                for(String s: args.split("\n"))
                    newEntries.add(s);
        }
        else if (action == 8) {
            String[] newEntry = newEntries.get(Integer.parseInt(args)).split(" ", 2);
            String url = newEntry[0];
            String desc = newEntry[1];
            newEntries.remove(Integer.parseInt(args));
            persist.set(0,url);
            this.query(1, desc);
        }
        else if (action == 9) {
            queryoutput.clear();
            writer.println("cluster");
            String clustertuning[] = args.split(" ",2);
            writer.println(clustertuning[0]);//numhops
            writer.println(clustertuning[1]);//numneigh
            String cansave = reader.readLine();
            String num_embeddings = reader.readLine();
            line = reader.readLine();
            nline = Integer.parseInt(line);
            queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of("-1", "", args,"","", "0", cansave,"", num_embeddings, persist.get(0)))));
            for(int i=0;i<nline;i++) {
                dburl = reader.readLine();
                dbdesc = reader.readLine();
                line = reader.readLine();
                String[] tokens = line.split(" ");
                queryoutput.add(new QueryResultSlice(new ArrayList<>(List.of(tokens[0], dburl, dbdesc,tokens[1],tokens[2],"0",QueryResultSlice.fetchSourceSite(dburl), tokens[3]))));
            }
        }
        return queryoutput;
    }
    public ArrayList<QueryResultSlice> GetPrivateNotesTags() {
        return privateNotesTags;
    }
}