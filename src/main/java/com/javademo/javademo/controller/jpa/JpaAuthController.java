package com.javademo.javademo.controller.jpa;

import com.javademo.javademo.jpa.dao.JpaUserDAO;
import com.javademo.javademo.jpa.domain.User;
import com.javademo.javademo.requestmodel.LoginModel;
import com.javademo.javademo.security.Auth;
import com.javademo.javademo.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("jpa")
public class JpaAuthController {

    private JpaUserDAO userDAO;

    @Autowired
    public JpaAuthController(JpaUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/login")
    public String login(Model model){
        LoginModel login = new LoginModel("","");
        model.addAttribute("login",login);
        return "jpa/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String loginP(LoginModel login, Model model, HttpSession session){
        User user = userDAO.login(login.getUsername(),login.getPassword());
        if (user==null){
            model.addAttribute("login",login);
            model.addAttribute("message","Wrong username or password");
            return "jpa/login";
        }
        else {
            session.setAttribute("isLogin", true);

            if (user.getGroupId()==Constant.ROLE_ADMIN)
                session.setAttribute("role", Auth.Role.ADMIN);
            else session.setAttribute("role", Auth.Role.LOGIN);

            session.setAttribute(Constant.USER_SESSION_KEY,user);
            return "redirect:/jpa/menu";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        //delete session
        session.setAttribute("isLogin",null);
        session.setAttribute("role",null);
        session.setAttribute(Constant.USER_SESSION_KEY,null);
        return "redirect:/jpa/login";
    }

    @GetMapping("/deny")
    public String deny(){
        return "deny";
    }
}
