package org.example.applestores.controller;

import org.example.applestores.model.CartItem;

import org.example.applestores.model.Order;
import org.example.applestores.model.OrderItem;
import org.example.applestores.model.User;
import org.example.applestores.repository.IOrderRepository;
import org.example.applestores.service.cart.ICartItemService;
import org.example.applestores.service.order.IOrderService;
import org.example.applestores.service.order.OrderService;
import org.example.applestores.service.product.IProductService;
import org.example.applestores.service.user.IUserService;


import org.example.applestores.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String listOrders(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = cartItemService.findByUser(user);
        model.addAttribute("orders", orders);
        return "/orders_list";
    }
    @GetMapping("/checkout-info")
    public String showCheckoutForm(@RequestParam("selectedIds") String selectedIds, Model model) {
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("order", new Order());
        return "/checkout-info"; // Tên file HTML bạn sẽ tạo
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute Order order,
                           @RequestParam("selectedIds") String selectedIds,
                           @SessionAttribute("loggedInUser") User user,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        // Xử lý danh sách sản phẩm được chọn
        List<Long> ids = Arrays.stream(selectedIds.split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<CartItem> carts = (List<CartItem>) session.getAttribute("cart");

        List<CartItem> selectedItems = carts.stream()
                .filter(item -> ids.contains(item.getProduct().getId())
                        && item.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm được chọn.");
            return "redirect:/user/home";
        }

        // Tạo đơn hàng
        order.setUser(user);
        order.setCustomerName(user.getUsername());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : selectedItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getQuantity() * item.getProduct().getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        double total = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalAmount(total);

        orderService.save(order);

        // Xoá sản phẩm đã đặt khỏi session cart
        carts.removeIf(item -> ids.contains(item.getProduct().getId()) && item.getUser().getId().equals(user.getId()));
        session.setAttribute("cart", carts);

        redirectAttributes.addFlashAttribute("successMessage", "Payment successful!");
        return "redirect:/user/home";
    }

    @GetMapping("/history")
    public String showOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.findByUser(user);
        model.addAttribute("orders", orders);
        return "/order-history";
    }


    @GetMapping("/detail/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.findById(id);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders/history";
        }

        model.addAttribute("order", order);
        return "/order-detail";
    }
}

