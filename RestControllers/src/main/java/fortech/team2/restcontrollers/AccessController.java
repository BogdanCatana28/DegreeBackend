package fortech.team2.restcontrollers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/test")
public class AccessController {
    /**
     * In controller methods you need to return a ResponseEntity<>, not a String
     */



    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/customer")
    /**
     * You could simply use @RolesAllowed({ "ROLE_CUSTOMER", "ROLE_MEDIC", "ROLE_ADMIN" })
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MEDIC') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/medic")
    @PreAuthorize("hasRole('MEDIC')")
    public String medicAccess() {
        return "Medic Board.";
    }
}