package com.javademo.javademo.controller;

import com.javademo.javademo.security.Auth;
import com.javademo.javademo.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping({"/","/menu"})
    @Auth(role=Auth.Role.LOGIN)
    //@RequestMapping(value="/",method=RequestMethod.GET)
    public String menu(Model model, HttpSession session){
        Auth.Role role = (Auth.Role)session.getAttribute("role");
        model.addAttribute("isAdmin", role==Auth.Role.ADMIN);
        return "menu";
    }
}
