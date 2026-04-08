package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account;


import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.UserService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.authentication.AuthenticationService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.UserService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.authentication.AuthenticationService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.UserService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/xxx/login")
public class LoginController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping()
    public String login(@RequestParam String username, @RequestParam String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("GOOD");
            System.out.println("User authenticated: " + authentication.getName());

            System.out.println("User id: " + authenticationService.getCurrentAuthenticatedUserId());
            for(GrantedAuthority authority:authenticationService.getCurrentAuthenticatedPrincipalDetails().getAuthorities()) {
                System.out.println("authority: " + authority.getAuthority());
            }


            if (session.isNew()) {
                System.out.println("New session established!");
            } else {
                System.out.println("Existing session active with ID: " + session.getId());
            }

            return "main";
        } catch (Exception e) {

            System.out.println("failed");
            System.out.println(e.getMessage());
            return "Login failed: " + e.getMessage();
        }
    }





//    @PostMapping()
//    public String login(@RequestParam String username, @RequestParam String password) {
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("GOOD");
//            System.out.println("User authenticated: " + authentication.getName());
//
//            System.out.println("User id: " + authenticationService.getCurrentAuthenticatedUserId());
//            for(GrantedAuthority authority:authenticationService.getCurrentAuthenticatedUserDetails().getAuthorities()) {
//                System.out.println("authority: " + authority.getAuthority());
//            }
//
//
//            if (session.isNew()) {
//                System.out.println("New session established!");
//            } else {
//                System.out.println("Existing session active with ID: " + session.getId());
//            }
//
//            return "main";
//        } catch (Exception e) {
//
//            System.out.println("failed");
//            System.out.println(e.getMessage());
//            return "Login failed: " + e.getMessage();
//        }
//    }

//    @PostMapping("/register")
//    public String register(@RequestParam String username, @RequestParam String password) {
//        userService.registerUser(username, password);
//        return "User registered successfully!";
//    }
//
//    @GetMapping("/welcome")
//    public String welcome() {
//        return "Welcome to the secured API!";
//    }
}
