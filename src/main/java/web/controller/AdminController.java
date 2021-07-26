package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping( "users")
    public String getAllUsers(Model model, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        model.addAttribute("username", username);
        model.addAttribute("userlist", userService.findAllUsers());
        return "userlist";
    }
    @PostMapping( "user-save")
    public String saveUser(@RequestParam(name = "role") String [] roles,
                           @RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "firstName") String firstName,
                           @RequestParam(name = "lastName") String lastName,
                           @RequestParam(name = "email") String email){

            List<Role> roles1 = Stream.of(roles).map(Role::new).collect(Collectors.toList());
            Set<Role> rol = new HashSet<>(roles1);

            User user = new User(username, password, firstName, lastName, email, rol);

            userService.saveUser(user);
            return "redirect:/admin/users";
    }
    @GetMapping( "user-save")
    public String saveUserPage(Model model) {
        model.addAttribute("roles", roleService.findAllRoles());

        return "saveuser";
    }

    @PostMapping( "user-update")
    public String updateUser(User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
    @GetMapping( "user-update")
    public String updateUserPage(Model model) {
        model.addAttribute("roles", roleService.findAllRoles());
        return "updateuser";
    }

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.findUser(id);
        userService.deleteUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("user")
    public String user(Model model, Authentication authentication) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            model.addAttribute("username", username);
        return "userpage";
    }



}
