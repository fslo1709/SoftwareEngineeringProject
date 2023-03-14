package com.example.restservice.Service;

import com.example.restservice.Model.Term;
import com.example.restservice.Repository.TermRepository;
import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermService {
    @Autowired
    TermRepository termRepository;

    public CommonResponse<List<Term>> handleTerm(String content) {
        List<Term> terms = new LinkedList<>();

        // Extract Terminologies
        List<String> extractedTerms = extractTerms(content);
        for (String name: extractedTerms) {
            Term term = termRepository.findItemByName(name);
            // If the Term does not exist in the database
            if (term == null) {
                Pair<String, String> info = getInfo(name);
                List <String> links = Arrays.asList(info.getValue());
                term = new Term(name, info.getKey(), links);
                termRepository.save(term);
            }
            terms.add(term);
        }
        Msg msg = new Msg("success", "get terms");
        return new CommonResponse<List<Term>> (msg, terms);
    }

    private List<String> extractTerms(String content) {
        List<String> terms = new LinkedList<>();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "./src/main/python/com/example/restservice/Service/TermExtract.py", content);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null)
                terms.add(line);
        }
        catch (java.lang.Exception e) {
            System.out.println(e);
        }
        return terms;
    }

    private Pair<String, String> getInfo(String term) {
        try {
            Gson gson = new Gson();
            String result = Jsoup.connect("https://www.googleapis.com/customsearch/v1?cx=4007a9bce1cc843ca&key=AIzaSyA1N7pE686fuZeCz4rngo5-gAEJYNNG3F0&q=" + term.replace(' ', '+')).ignoreContentType(true).execute().body();
            JsonObject json = gson.fromJson(result, JsonObject.class);
            JsonObject item = json.getAsJsonArray("items").get(0).getAsJsonObject();
            String description = item.get("snippet").getAsString();
            String link = item.get("link").getAsString();
            return new Pair<String, String> (description, link);
        }
        catch (java.lang.Exception e) {
            System.out.println(e);
        }
        return new Pair<String, String> ("", "");
    }
}
