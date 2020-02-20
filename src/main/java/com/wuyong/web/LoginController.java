package com.wuyong.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("login")
    public String login(){
        System.out.println("11111111111");
        return "redirect:login.jsp";
    }

}
