package org.example.applestores.controller;

import org.example.applestores.model.*;

import org.example.applestores.repository.IOrderRepository;
import org.example.applestores.repository.IProductRepository;
import org.example.applestores.service.cart.CartItemService;
 import org.example.applestores.service.order.IOrderService;
import org.example.applestores.service.product.ProductService;
import org.example.applestores.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cartController")
public class CartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private IOrderService orderService;

    @GetMapping("/cartView")
    public String viewCart(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<CartItem> cartItems = cartItemService.findCartByUser(session, model);

        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "Cart is empty!");
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

        int cartItemCount = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "/cart"; // thymeleaf template
    }


    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session, Model model) {
        cartItemService.removeById(id, session, model);
        return "redirect:/cartController/cartView";
    }
}


//    @PostMapping("/cart/checkout")
//    public String checkout(@RequestParam(name = "selectedItems", required = false) List<Long> selectedProductIds,
//                           Principal principal,
//                           RedirectAttributes redirectAttributes) {
//        if (selectedProductIds == null || selectedProductIds.isEmpty()) {
//            redirectAttributes.addFlashAttribute("successMessage", "Bạn chưa chọn sản phẩm để đặt hàng.");
//            return "redirect:/cartController/cartView";
//        }
//
//        User user = userService.findByEmail(principal.getName()); // hoặc lấy từ session
//        List<CartItem> cartItems = cartItemRepository.findByUser(user);
//
//        List<CartItem> selectedItems = cartItems.stream()
//                .filter(item -> selectedProductIds.contains(item.getProduct().getId()))
//                .collect(Collectors.toList());
//
//        if (selectedItems.isEmpty()) {
//            redirectAttributes.addFlashAttribute("successMessage", "Không có sản phẩm nào được chọn.");
//            return "redirect:/cartController/cartView";
//        }
//
//        Order order = new Order();
//        order.setUser(user);
//        order.setCustomerName(user.getUsername()); // hoặc để người dùng nhập ở form
//        order.setOrderDate(LocalDateTime.now());
//
//        List<OrderItem> orderItems = new ArrayList<>();
//        double total = 0;
//        for (CartItem cartItem : selectedItems) {
//            Product product = cartItem.getProduct();
//            total += product.getPrice() * cartItem.getQuantity();
//            if (product.getStock() < cartItem.getQuantity()) {
//                redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm " + product.getName() + " không đủ hàng.");
//                return "redirect:/cartController/cartView";
//            }
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProduct(product);
//            orderItem.setQuantity(cartItem.getQuantity());
//            orderItem.setPrice(((Product) product).getPrice() * cartItem.getQuantity());
//            orderItem.setOrder(order);
//
//            orderItems.add(orderItem);
//
//            // Cập nhật tồn kho
//            product.setStock(product.getStock() - cartItem.getQuantity());
//            productService.save(product);
//        }
//
//        order.setItems(orderItems);
////        order.setTotalAmount(total);
//        orderService.save(order);
//
//        // Xoá sản phẩm đã đặt khỏi giỏ hàng
//        for (CartItem item : selectedItems) {
//            cartItemRepository.deleteByUserAndProductId(user, item.getProduct().getId());
//        }
//
//        redirectAttributes.addFlashAttribute("successMessage", "Order successful!");
//        return "redirect:/cartController/cartView";
//    }
//}
//    @PostMapping("/checkout")
//    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
//        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
//
//        if (cart == null || cart.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Giỏ hàng đang trống.");
//            return "redirect:/cart/view";
//        }
//
//        Order orders = new Order();
//        orders.setCreatedAt(LocalDateTime.now());
//        orders.setCustomerName("Khách hàng"); // hoặc lấy từ người dùng đăng nhập
//
//        List<OrderItem> items = new ArrayList<>();
//        for (CartItem cartItem : cart.values()) {
//            OrderItem item = new OrderItem();
//            item.setProduct(cartItem.getProduct());
//            item.setQuantity(cartItem.getQuantity());
//            item.setPrice(cartItem.getProduct().getPrice());
//            item.setOrder(orders);
//            items.add(item);
//        }
//
//        orders.setItems(items);
//        orderRepository.save(orders); // cascade sẽ tự lưu order_items
//
//        session.removeAttribute("cart"); // xoá giỏ hàng
//        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
//        return "redirect:/order/success";
//    }
//}

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


