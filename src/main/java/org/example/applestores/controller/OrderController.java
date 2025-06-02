//package org.example.applestores.controller;
//
//import org.example.applestores.model.CartItem;
//import org.example.applestores.model.Order;
//import org.example.applestores.model.User;
//import org.example.applestores.service.cart.ICartItemService;
//import org.example.applestores.service.order.IOrderService;
//import org.example.applestores.service.user.IUserService;
//import org.example.applestores.service.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.http.HttpSession;
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/order")
//public class OrderController {
//@Autowired
//    private ICartItemService<CartItem> cartItemService;
//
//    @Autowired
//    private IOrderService<Order> orderService;
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/checkout")
//    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
//        User currentUser = (User) session.getAttribute("loggedInUser");
//        if (currentUser == null) {
//            return "redirect:/login"; // Hoặc xử lý chưa đăng nhập
//        }
//
//        List<CartItem> cartItems = cartItemService.findAllByUser(currentUser);
//        if (cartItems.isEmpty()) {
//            redirectAttributes.addFlashAttribute("successMessage", "Giỏ hàng đang trống.");
//        } else {
//            orderService.checkout(currentUser, cartItems);
//
//            // Tạo thông báo gồm tên các sản phẩm đã thanh toán
//            String productNames = cartItems.stream()
//                    .map(item -> item.getProduct().getName())
//                    .collect(Collectors.joining(", "));
//            redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công cho: " + productNames);
//        }
//
//        return "redirect:/cart/view";
//    }
//}
//
