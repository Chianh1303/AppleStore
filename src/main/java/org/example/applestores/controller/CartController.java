package org.example.applestores.controller;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.OrderSession;
import org.example.applestores.repository.IProductRepository;
import org.example.applestores.service.cart.CartItemService;
import org.example.applestores.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cartController")
public class CartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private IProductRepository productRepository;

    @GetMapping("/cartView")
    public String viewCart(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<CartItem> cartItems = cartItemService.findByUser(session, model);

        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "Giỏ hàng rỗng !");
            return "redirect:/user/home";
        }

        // Cập nhật lại giá sản phẩm từ DB
        for (CartItem item : cartItems) {
            Long productId = item.getProduct().getId();
            productRepository.findById(productId).ifPresent(item::setProduct);
        }

        // Tính tổng tiền
        long totalAmount = cartItems.stream()
                .mapToLong(item -> (long) (item.getQuantity() * item.getProduct().getPrice()))
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "/cart"; // thymeleaf template
    }


    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id,HttpSession session,Model model) {
        cartItemService.removeById(id,session,model);
        return "redirect:/cartController/cartView";
    }
//    @GetMapping("/checkoutAll")
//    public String checkoutAll(HttpSession session, RedirectAttributes redirectAttributes) {
//        cartItemService.checkoutAndStoreInSession(session);
//        redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công!");
//        return "redirect:/cartController/cartView";
//    }
//    @GetMapping("/orderSession/view")
//    public String viewOrderSession(Model model, HttpSession session) {
//        OrderSession order = (OrderSession) session.getAttribute("orderSession");
//        if (order == null) {
//            model.addAttribute("message", "Không có đơn hàng nào trong session.");
//            return "/orderSessionView";
//        }
//        model.addAttribute("order", order);
//        return "/orderSessionView";
//    }


//    @GetMapping("/checkout")
//    public String checkoutAll(Model model, HttpSession session) {
//        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
//        // Xử lý thanh toán tất cả các mục trong giỏ hàng
//        cartItemService.checkoutAll(cartItems);
//        model.addAttribute("successMessage", "Thanh toán thành công!");
//        return "redirect:/cart";
//    }

}
