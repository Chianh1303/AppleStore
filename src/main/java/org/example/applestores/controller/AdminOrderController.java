package org.example.applestores.controller;

import org.example.applestores.model.Order;
import org.example.applestores.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    // Danh sách đơn hàng
    @GetMapping
    public String viewOrderList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "/admin/order-list";
    }

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderService.save(order);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
        }
        return "redirect:/admin/orders";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderService.findById(id);
        if (order == null) {
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        return "/order-admin";
    }
}

