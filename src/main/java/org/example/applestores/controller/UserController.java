package org.example.applestores.controller;

import org.example.applestores.model.Product;
import org.example.applestores.model.User;
import org.example.applestores.service.cart.ICartItemService;
import org.example.applestores.service.product.IProductService;
import org.example.applestores.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public String showHomePage(HttpSession session, Model model) {
        List<Product> products = productService.findAll();

        // Lọc sản phẩm nổi bật (ngẫu nhiên + còn hàng)
        List<Product> featuredProducts = new ArrayList<>(products);
        Collections.shuffle(featuredProducts);
        featuredProducts = featuredProducts.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .limit(8)
                .collect(Collectors.toList());

        // Lọc sản phẩm giảm giá (giá thấp nhất + còn hàng)
        List<Product> discountedProducts = products.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .limit(8)
                .collect(Collectors.toList());
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("discountedProducts", discountedProducts);
        model.addAttribute("products", products);
        model.addAttribute("isSearching", false);



        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "/user-view";
    }



    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Product product = (Product) productService.findById(id);
        String result = cartItemService.addToCart(product, quantity, session, redirectAttributes);

        if (result != null) return result; // Nếu có lỗi về tồn kho sẽ redirect về trang trước
        return "redirect:/user/home"; // Thành công thì về trang chính
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
        model.addAttribute("isSearching", true);
        return "/user-view";
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
        return "/user-view";  // view name
    }
@GetMapping("/introduce")
public String introduce(Model model) {
    return "/about";
}

}
