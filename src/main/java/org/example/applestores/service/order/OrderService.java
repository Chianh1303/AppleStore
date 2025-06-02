package org.example.applestores.service.order;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.Orders;
import org.example.applestores.model.OrderDetail;
import org.example.applestores.model.User;
import org.example.applestores.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService<Orders> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public void save(Orders order) {
        orderRepository.save(order);
    }

    @Override
    public List<Orders> findAll() {
        return orderRepository.findAllByOrderByIdDesc(); // giống với productService
    }

    @Override
    public Orders findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    public void checkout(User user, List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) return;

        Orders order = new Orders();
        order.setUser(user);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus("PAID");

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(cartItem.getProduct());
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getProduct().getPrice());
            orderDetails.add(detail);
        }

        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
    }
}

