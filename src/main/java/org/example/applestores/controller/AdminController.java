package org.example.applestores.controller;

import org.example.applestores.model.Product;
import org.example.applestores.model.User;
import org.example.applestores.service.product.ProductService;
import org.example.applestores.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/homeAdmin")
    public String showHomePage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/admin";
    }

    @GetMapping("/search_admin")
    public String showProductList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> products = (keyword != null && !keyword.isEmpty())
                ? productService.searchByName(keyword)
                : productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "/admin";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "/edit-prd";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/homeAdmin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "/edit-prd";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/homeAdmin";
    }

    @GetMapping("/detailAdmin/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "/detail-admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/userManagement")
    public String viewUserList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/user-management";
    }
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/add-User";
    }

    @PostMapping("/user/save")
    public String saveNewUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin/user-management";
    }


    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/edit-user";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin/user-management";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/user-management";
    }
}


