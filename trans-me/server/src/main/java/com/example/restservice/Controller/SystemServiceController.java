package com.example.restservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.SystemService;
import com.example.restservice.Service.Payload.Payload;

@RestController
@RequestMapping("/system")
public class SystemServiceController {
    
	@Autowired
	SystemService systemService;

	// reset
	@DeleteMapping("/reset")
	public CommonResponse reset(){

		Payload <Msg, String> result = systemService.reset();
		
		System.out.println(result);
		return new CommonResponse<String>(
			result.getMsg(),
			result.getData()
			);
	}
	
}