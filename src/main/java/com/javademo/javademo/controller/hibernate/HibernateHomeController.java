package com.javademo.javademo.controller.hibernate;

import com.javademo.javademo.security.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("hibernate")
public class HibernateHomeController {
    @GetMapping({"/","/menu"})
    @Auth(role=Auth.Role.LOGIN)
    //@RequestMapping(value="/",method=RequestMethod.GET)
    public String menu(Model model, HttpSession session){
        Auth.Role role = (Auth.Role)session.getAttribute("role");
        model.addAttribute("isAdmin", role==Auth.Role.ADMIN);
        return "/hibernate/menu";
    }
}
