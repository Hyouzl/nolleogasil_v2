package com.fourroro.nolleogasil_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

/*
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, world!!";
    }
*/

/*
    @GetMapping("/test")
    public String test(@RequestParam(value="id", required=false) Integer id, @RequestParam(value="title", required=false) String title){
        System.out.println("id>>>>>>" + id);
        System.out.println("title>>>>>>" + title);

        return title;
    }
*/

    @GetMapping("/")
    public String index() {
        return "놀러가실?";
    }
}