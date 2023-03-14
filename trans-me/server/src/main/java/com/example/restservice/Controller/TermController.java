package com.example.restservice.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Model.Term;
import com.example.restservice.Request.term.PostTermRequest;
import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Service.TermService;

@RestController
public class TermController {

    @Autowired
    TermService termService;

	@PostMapping("/term")
	public CommonResponse term(@RequestBody PostTermRequest req) {
        String content = req.getData().getContent();

		CommonResponse<List<Term>> ret = termService.handleTerm(content);
        return ret;
	}
}
