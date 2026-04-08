package hu.kalmancheysandor.applications.dominions.servers.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/test")
@Slf4j
public class TestController {


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void access(@PathVariable("uuid") String uuid) {
        System.out.println("Test endpoint invocated!");




        // A bejelentkezett felhasználó adatainak elérése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String username = authentication.getName();  // A felhasználó neve
            System.out.println("Bejelentkezett felhasználó: " + username);

            // Felhasználói jogosultságok ellenőrzése
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("Jogosultság: " + authority.getAuthority());
            });
        }
        else{
            System.out.println("Nincs authentikalva");
        }



    }

}
