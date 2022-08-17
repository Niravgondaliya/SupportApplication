package com.support.me.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.support.me.service.GetPostRequestService;

import java.util.Map;


@RestController
@RequestMapping("/support")
public class QueryController {

    @Autowired
    private GetPostRequestService requestService;


    @PostMapping("/API/WEB")
    public String runWeb(@RequestHeader Map<String,String> headers, @RequestBody String listSt, @RequestParam(name = "type") final String type) {
       System.out.println(headers);
        return requestService.getResponse(listSt, type, headers);
    }
    
   

}
