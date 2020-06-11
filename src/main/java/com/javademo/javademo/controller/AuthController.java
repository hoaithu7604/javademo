package com.javademo.javademo.controller;

import com.javademo.javademo.jdbc.bo.UserBO;
import com.javademo.javademo.jdbc.dto.UserDTO;
import com.javademo.javademo.model.User;
import com.javademo.javademo.requestmodel.CreateUserModel;
import com.javademo.javademo.requestmodel.LoginModel;
import com.javademo.javademo.security.Auth;
import com.javademo.javademo.util.Constant;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model){
        LoginModel login = new LoginModel("","");
        model.addAttribute("login",login);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String loginP(LoginModel login, Model model, HttpSession session){
        UserBO userBO = new UserBO();
        UserDTO user = userBO.doLogin(login.getUsername(),login.getPassword());
        if (user==null){
            model.addAttribute("login",login);
            model.addAttribute("message","Wrong username or password");
            return "login";
        }
        else {
            session.setAttribute("isLogin", true);

            if (user.getGroupId()==Constant.ROLE_ADMIN)
                session.setAttribute("role", Auth.Role.ADMIN);
            else session.setAttribute("role", Auth.Role.LOGIN);

            session.setAttribute(Constant.USER_SESSION_KEY,user);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        //delete session
        session.setAttribute("isLogin",null);
        session.setAttribute("role",null);
        session.setAttribute(Constant.USER_SESSION_KEY,null);
        return "redirect:/login";
    }

    @GetMapping("/deny")
    public String deny(){
        return "deny";
    }
}
