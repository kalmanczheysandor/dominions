package hu.kalmancheysandor.applications.dominions.servers.admin.controller.auth;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth.LoginRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.UserService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/auth")
public class AuthController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public String login(@RequestParam String username, @RequestParam String password) {
//        System.out.println("AUTH-LOGIN");
//        if (authenticationService.login(username, password)) {
//            return "main";
//        } else {
//            System.out.println("failed");
//            return "Login failed: ";
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("AUTH-LOGIN");
        return ResponseEntity.ok(null);
    }

}
