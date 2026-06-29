package com.app.ewallet.controller;

import com.app.ewallet.model.User;
import com.app.ewallet.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() { return "index"; }

    // Register
    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           Model model) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User saved = userService.saveUser(user);
        if (saved == null) {
            model.addAttribute("error", "Email already registered! Please login.");
            return "register";
        }
        return "redirect:/login";
    }

    // Login
    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.login(email, password);
        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        session.setAttribute("email", email);
        return "redirect:/dashboard";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        model.addAttribute("balance", userService.getBalance(email));
        return "dashboard";
    }

    @PostMapping("/addMoney")
    public String addMoney(@RequestParam double amount, HttpSession session) {
        String email = (String) session.getAttribute("email");
        userService.addMoney(email, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String receiver,
                           @RequestParam double amount,
                           HttpSession session) {
        String sender = (String) session.getAttribute("email");
        userService.transfer(sender, receiver, amount);
        return "redirect:/dashboard";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("transactions", userService.getHistory(email));
        return "history";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
