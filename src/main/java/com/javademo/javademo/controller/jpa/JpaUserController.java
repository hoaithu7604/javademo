package com.javademo.javademo.controller.jpa;

import com.javademo.javademo.jpa.domain.User;
import com.javademo.javademo.jpa.dao.JpaUserDAO;
import com.javademo.javademo.requestmodel.ChangePasswordModel;
import com.javademo.javademo.requestmodel.CreateUserModel;
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

@Controller()
@RequestMapping("jpa/user")
public class JpaUserController {

    private JpaUserDAO userDAO;

    @Autowired
    public JpaUserController(JpaUserDAO jpaUserDAO) {
        this.userDAO = jpaUserDAO;
    }

    @Auth(role=Auth.Role.ADMIN)
    @GetMapping("/create")
    public String create(Model model){
        CreateUserModel user = new CreateUserModel("","","","","","","","","",0);
        model.addAttribute("user",user);
        return "jpa/user-create";
    }

    @Auth(role=Auth.Role.ADMIN)
    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createP(CreateUserModel user, Model model){

        if ("".equals(user.getUsername())||"".equals(user.getPassword())||"".equals(user.getPassword2()))
        {
            model.addAttribute("user", user);
            model.addAttribute("message","Username and password cannot be empty.");
        }
        else if (!user.getPassword().equals(user.getPassword2()))
        {
            model.addAttribute("user", user);
            model.addAttribute("message","Password not match");
        }
        else {
            User _user = userDAO.findByUsername(user.getUsername());
            if (_user != null) {
                model.addAttribute("user", user);
                model.addAttribute("message", "Username already exists");
            } else {
                userDAO.addUser(new User(user.getUsername(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getSex(),
                        user.getAddress(),
                        user.getEmail(),
                        user.getMobilePhone(), 1));
                model.addAttribute("message", "Create user successfully");
                CreateUserModel emptyuser = new CreateUserModel("", "", "", "", "", "", "", "", "", 0);
                model.addAttribute("user", emptyuser);
                //}
            }
        }
        return "jpa/user-create";
    }

    @Auth(role=Auth.Role.ADMIN)
    @GetMapping("/search")
    public String search(Model model){
        User search = new User("","","","","","","","",0);
        model.addAttribute("users",userDAO.getAll());
        model.addAttribute("search",search);
        return "jpa/user-search";
    }

    @Auth(role=Auth.Role.ADMIN)
    @RequestMapping(value = "/search", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String searchP(User search, Model model){
        model.addAttribute("users", userDAO.get(
                search.getUsername(),
                Integer.toString(search.getGroupId()),
                search.getFirstName(),
                search.getLastName(),
                search.getSex(),
                search.getAddress(),
                search.getEmail(),
                search.getMobilePhone()
        ));
        model.addAttribute("search",search);
        return "jpa/user-search";
    }

    @Auth(role=Auth.Role.LOGIN)
    @GetMapping("/info")
    public String info(Model model, HttpSession session){
        User user = (User)session.getAttribute(Constant.USER_SESSION_KEY);
        model.addAttribute("user",user);
        return "jpa/user-info";
    }

    @Auth(role=Auth.Role.LOGIN)
    @RequestMapping(value = "/info", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String infoP(User user, Model model, HttpSession session){
        User newUser = (User)session.getAttribute(Constant.USER_SESSION_KEY);
        userDAO.update(newUser.getUsername(),user.getFirstName(),user.getLastName(),user.getSex(),user.getAddress(),user.getEmail(),user.getMobilePhone());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setSex(user.getSex());
        newUser.setAddress(user.getAddress());
        newUser.setEmail(user.getEmail());
        newUser.setMobilePhone(user.getMobilePhone());
        session.setAttribute(Constant.USER_SESSION_KEY,newUser);

        model.addAttribute("user",user);
        model.addAttribute("message","Update successfully");
        return "jpa/user-info";
    }

    @Auth(role=Auth.Role.LOGIN)
    @GetMapping("/change-password")
    public String changePassword(Model model){
        ChangePasswordModel changeModel = new ChangePasswordModel("","","");
        model.addAttribute("model",changeModel);
        return "jpa/change-password";
    }

    @Auth(role=Auth.Role.LOGIN)
    @RequestMapping(value = "/change-password", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String changePasswordP(ChangePasswordModel changeModel, Model model, HttpSession session){
        User user = (User)session.getAttribute(Constant.USER_SESSION_KEY);
        User getUser = userDAO.login(user.getUsername(),changeModel.getOldPassword());
        if (getUser==null){
            model.addAttribute("model",changeModel);
            model.addAttribute("message","Wrong Old Password");
        }
        else if (!changeModel.getPassword().equals(changeModel.getPassword2()))
        {
            model.addAttribute("model",changeModel);
            model.addAttribute("message","Password not match");
        }
        else {
            userDAO.changPassword(user.getUsername(),changeModel.getPassword());
            model.addAttribute("message","Update successfully");
            model.addAttribute("model",new ChangePasswordModel("","",""));
        }
        return "jpa/change-password";
    }
}
