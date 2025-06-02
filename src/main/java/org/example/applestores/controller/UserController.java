package org.example.applestores.controller;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.Product;
import org.example.applestores.model.User;
import org.example.applestores.service.cart.ICartItemService;
import org.example.applestores.service.product.IProductService;
import org.example.applestores.service.product.ProductService;
import org.example.applestores.service.user.IUserService;
import org.example.applestores.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private IUserService userService;


    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/userView";
    }


    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(name = "quantity",defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Product product = (Product) productService.findById(id);
        String x = cartItemService.addToCart(product, quantity, session, redirectAttributes);
        if (x != null) return x;
        return "redirect:/user/home";
    }



    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = (Product) productService.findById(id);
        model.addAttribute("product", product);
        return "/detail";
    }
    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productService.searchByName(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "/userView";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("featuredProducts", productService.getFeaturedProducts());
        model.addAttribute("normalProducts", productService.getNormalProducts());
        return "/userView";  // view name
    }


}
