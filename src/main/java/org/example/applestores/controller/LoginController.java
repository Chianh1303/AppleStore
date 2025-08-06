package org.example.applestores.controller;

import org.example.applestores.model.User;
import org.example.applestores.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

        @GetMapping("")
        public String loginForm(Model model) {
            model.addAttribute("user", new User());
            return "/login";
        }

        @PostMapping("/signin")
        public String login(@ModelAttribute("user") User user, HttpSession session, Model model) {
            User found = userService.login(user.getUsername(), user.getPassword());
            if (found != null) {
                session.setAttribute("loggedInUser", found);
                if ("ADMIN".equalsIgnoreCase(found.getRole())) {
                    return "redirect:/admin/homeAdmin";

                } else {
                    return "redirect:/user/home";
                }
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "/login";
            }
        }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "/register";
        }

        user.setRole("user"); // ✅ Gán mặc định role là "user"

        userService.save(user);
        return "redirect:/login";
    }




}
