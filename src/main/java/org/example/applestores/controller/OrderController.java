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
    @PostMapping("/checkout")
    public String checkout(@RequestParam("selectedIds") String selectedIds,
                           @SessionAttribute("loggedInUser") User user,
                           RedirectAttributes redirectAttributes, HttpSession session) {

        // B1: Tách chuỗi selectedIds thành List<String>
        String[] idStrings = selectedIds.split(",");
        List<String> validIdStrings = Arrays.stream(idStrings)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<Long> ids = validIdStrings.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<CartItem> allCartItems = new ArrayList<>();

        List<CartItem> carts = (List<CartItem>) session.getAttribute("cart");
        for(CartItem item : carts) {
            if(Objects.equals(item.getUser().getId(), user.getId())) {
                allCartItems.add(item);
            }
        }

        Set<Long> selectedIdSet = new HashSet<>(ids);
        List<CartItem> selectedItems = allCartItems.stream()
                .filter(item -> selectedIdSet.contains(item.getProduct().getId()))
                .collect(Collectors.toList());

        if (selectedItems == null || selectedItems.isEmpty()) {
            System.out.println("Không có sản phẩm được chọn.");
            redirectAttributes.addFlashAttribute("successMessage", "Vui lòng chọn sản phẩm để thanh toán.");
            return "redirect:/user/home";
        }


        Order order = new Order();
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

        double totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        order.setTotalAmount(totalAmount);
        orderService.save(order);
        if (carts != null) {
            // Xóa những sản phẩm đã thanh toán khỏi session
            carts.removeIf(item -> selectedIdSet.contains(item.getProduct().getId())
                    && Objects.equals(item.getUser().getId(), user.getId()));
            // Cập nhật lại vào session
            session.setAttribute("cart", carts);
        }


        redirectAttributes.addFlashAttribute("successMessage", "Order successful!");
        return "redirect:/user/home";
    }
    // Xem lịch sử đơn hàng
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

