package hu.kalmancheysandor.applications.dominion.server.web.controller;

import hu.kalmancheysandor.applications.dominion.server.web.entity.User;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(User user) {
        User savedUser = userService.saveAsPlayer(user);

        return "redirect:/login";
    }
}
