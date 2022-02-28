package ru.kata.spring.boot_security.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model,
                            @ModelAttribute("user") User user) {
        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @PostMapping("/save")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") ArrayList<Long> roles) {

        Set<Role> roleSet = new HashSet<>((Collection<? extends Role>)
                roleService.findAllById(roles));
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/admin";
    }
    @GetMapping("/edit/{id}")
    public void getEditUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        //return "redirect:/admin";
    }
    @PutMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user, Model model, @RequestParam("listRoles") ArrayList<Long> roles) {
        model.addAttribute("roles", roleService.findAll());
        Set<Role> roleSet = new HashSet<>((Collection<? extends Role>)
                roleService.findAllById(roles));
        user.setRoles(roleSet);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/remove")
    public String userDelete (@PathVariable(value = "id") Long id, Model model) {
        User user = userService.findById(id);
        userService.delete(user);
        return "redirect:/admin";
    }
    @GetMapping("/user")
    public String pageForUser(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        return "user";
    }

    @GetMapping("/findUser")
    @ResponseBody
    public User findUserByID(Long id) {
        return userService.findById(id);
    }
}

