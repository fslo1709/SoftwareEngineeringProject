package com.example.restservice.Service;

import java.lang.Exception;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {
    public CommonResponse<String> handleSummary(String content) throws IOException {
        String data = "";
        var processBuilder = new ProcessBuilder("python","-W ignore"
            , "./src/main/python/com/example/restservice/Service/Summary.py"
            , content);
        processBuilder.redirectErrorStream(true);
        var process = processBuilder.start();
        try(var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream() ))){
                    String line ;
                    while( (line = reader.readLine()) != null){
                       //System.out.println(line);
                        data = data + line;
                    }
                }
        catch (Exception e) {
            e.printStackTrace();
        }
        Msg msg = new Msg("success", "get summary");
        //System.out.println(data);
        return new CommonResponse<String>(msg,data);
    }
}
