package com.csw.controller;

import com.csw.dao.FolderDao;
import com.csw.dao.UserDao;
import com.csw.entity.User;
import com.csw.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("lr")
public class LoginAndRegister {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FolderDao folderDao;

    @RequestMapping("login")
    public String Login(String username, String password,
                        HttpSession session, Model model, HttpServletRequest request) throws IOException {
        User user = userDao.selectUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return "/index";
        } else {
            if (user.getStatus() == User.STATUS_NEW) {
                folderDao.addRootFolder(user);
                String path = SystemConstant.USER_FILE_PATH;
                File folder = new File(path + "\\" + user.getUsername());
                if (!folder.exists())
                    folder.mkdirs();
                userDao.changeUserStatus(user.getId());
            }
            session.setAttribute("user", user);
            return "/view/main";
        }
    }

    @RequestMapping("register")
    public String Register(@RequestParam("username") String username, @RequestParam("password") String password,
                           @RequestParam("rePassword") String rePassword, @RequestParam("email") String email, Model model) throws IOException {
        if (username == null || username.equals("")) {
            model.addAttribute("sysMsg", "用户名为空");
            return "/index";
        }
        if (password == null || password.equals("")) {
            model.addAttribute("sysMsg", "密码为空");
            return "/index";
        }
        if (rePassword == null || rePassword.equals("")) {
            model.addAttribute("sysMsg", "第二次输入的密码为空");
            return "/index";
        }
        if (email == null || email.equals("")) {
            model.addAttribute("sysMsg", "邮箱为空");
            return "/index";
        }
        if (username.length() < 5 || username.length() > 30) {
            model.addAttribute("sysMsg", "用户名小于五位或者大于30位");
            return "/index";
        }
        if (password.length() < 5 || password.length() > 30) {
            model.addAttribute("sysMsg", "密码小于五位或者大于30位");
            return "/index";
        }
        if (!password.equals(rePassword)) {
            model.addAttribute("sysMsg", "两次输入的密码不一致");
            return "/index";
        }
        
        User user = new User(username, password, email);
        userDao.addUser(user);
        return "/index";
    }
}
