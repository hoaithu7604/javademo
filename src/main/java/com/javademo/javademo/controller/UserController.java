package com.javademo.javademo.controller;

import com.javademo.javademo.jdbc.bo.UserBO;
import com.javademo.javademo.jdbc.dto.UserDTO;
import com.javademo.javademo.model.User;
import com.javademo.javademo.requestmodel.ChangePasswordModel;
import com.javademo.javademo.requestmodel.CreateUserModel;
import com.javademo.javademo.security.Auth;
import com.javademo.javademo.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping("user")
public class UserController {

    @Auth(role=Auth.Role.ADMIN)
    @GetMapping("/create")
    public String create(Model model){
        CreateUserModel user = new CreateUserModel("","","","","","","","","",0);
        model.addAttribute("user",user);
        return "user-create";
    }

    @Auth(role=Auth.Role.ADMIN)
    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createP(CreateUserModel user, Model model){
        //model.addAttribute("message","Insert successfully");
        UserBO userBO = new UserBO();
        List<String> errorList = userBO.doValidateUser(user.getUsername(),user.getPassword(),user.getPassword2());
        if (errorList.size()>0) {
            model.addAttribute("user",user);
            model.addAttribute("message","Error");
        }
        else{
            userBO.doCreateNewUser(user.getUsername(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getSex(),
            user.getAddress(),
            user.getEmail(),
            user.getMobilePhone());
            model.addAttribute("message","Create user successfully");
            CreateUserModel emptyuser = new CreateUserModel("","","","","","","","","",0);
            model.addAttribute("user",emptyuser);
        }
        return "user-create";

    }


    @Auth(role=Auth.Role.ADMIN)
    @GetMapping("/search")
    public String search(Model model){
        List<User> users = new ArrayList<User>();
        User search = new User("","","","","","","","",0);
        UserBO userBO = new UserBO();
        model.addAttribute("users",userBO.doGetAllUser());
        model.addAttribute("search",search);
        return "user-search";
    }

    @Auth(role=Auth.Role.ADMIN)
    @RequestMapping(value = "/search", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String searchP(User search, Model model){
        UserBO userBO = new UserBO();
        model.addAttribute("users", userBO.doSearchUser(
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
        return "user-search";
    }

    @Auth(role=Auth.Role.LOGIN)
    @GetMapping("/info")
    public String info(Model model, HttpSession session){
        UserDTO user = (UserDTO)session.getAttribute(Constant.USER_SESSION_KEY);
        model.addAttribute("user",user);
        return "user-info";
    }

    @Auth(role=Auth.Role.LOGIN)
    @RequestMapping(value = "/info", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String infoP(User user, Model model, HttpSession session){
        UserBO userBO = new UserBO();
        UserDTO newUser = (UserDTO)session.getAttribute(Constant.USER_SESSION_KEY);
        userBO.doUpdateUserInfo(newUser.getUsername(),user.getFirstName(),user.getLastName(),user.getSex(),user.getAddress(),user.getEmail(),user.getMobilePhone());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setSex(user.getSex());
        newUser.setAddress(user.getAddress());
        newUser.setEmail(user.getEmail());
        newUser.setMobilePhone(user.getMobilePhone());
        session.setAttribute(Constant.USER_SESSION_KEY,newUser);

        model.addAttribute("user",user);
        model.addAttribute("message","Update successfully");
        return "user-info";
    }

    @Auth(role=Auth.Role.LOGIN)
    @GetMapping("/change-password")
    public String changePassword(Model model){
        ChangePasswordModel changeModel = new ChangePasswordModel("","","");
        model.addAttribute("model",changeModel);
        return "change-password";
    }

    @Auth(role=Auth.Role.LOGIN)
    @RequestMapping(value = "/change-password", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String changePasswordP(ChangePasswordModel changeModel, Model model, HttpSession session){
        UserBO userBO = new UserBO();
        UserDTO user = (UserDTO)session.getAttribute(Constant.USER_SESSION_KEY);
        UserDTO getUser = userBO.doLogin(user.getUsername(),changeModel.getOldPassword());
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
            userBO.doUpdateUserPassword(user.getUsername(),changeModel.getPassword());
            model.addAttribute("message","Update successfully");
            model.addAttribute("model",new ChangePasswordModel("","",""));
        }
        return "change-password";
    }
}
