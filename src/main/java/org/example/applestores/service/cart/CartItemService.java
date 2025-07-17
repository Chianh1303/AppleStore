package org.example.applestores.service.cart;

import org.example.applestores.model.*;
import org.example.applestores.repository.IOrderDetailRepository;
import org.example.applestores.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    //
//    @Override
//    public void save(CartItem cartItem) {
//        cartItemRepository.save(cartItem);
//    }
//
    @Override
    public List<CartItem> findCartByUser(HttpSession session, Model model) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cart");
        if (list == null) {
            return null;
        }
        User user = (User) session.getAttribute("loggedInUser");
        List<CartItem> itemList = new ArrayList<>();
        for (CartItem item : list) {
            if (item.getUser() == user) {
                itemList.add(item);
            }
        }
        return list;
    }

    @Override
    public void removeById(Long id, HttpSession session, Model model) {
        findCartByUser(session, model).removeIf(item -> item.getProduct().getId() == id);
    }

    @Override
    public void checkoutAllFromSession(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cartItems == null || cartItems.isEmpty()) return;

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum());

        orderRepository.save(order); // ✅ Lưu đơn hàng

        // Nếu có OrderDetail:
        for (CartItem cartItem : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order); // Gắn order vào chi tiết
            detail.setProduct(cartItem.getProduct());
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getProduct().getPrice());

            orderDetailRepository.save(detail); // ✅ Lưu từng dòng sản phẩm
        }

        // Xóa giỏ hàng khỏi session sau khi thanh toán
        session.removeAttribute("cart");
    }


    //
//    @Override
//    public List<CartItem> findAllByUser(User user) {
//        return cartItemRepository.findAllByUser(user);
//    }
//
//    @Override
//    public CartItem findByUserAndProduct(User user, Product product) {
//        return cartItemRepository.findByUserAndProduct(user, product);
//    }
    @Override
    public String addToCart(Product product, int quantity, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");

        // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);  // Cộng dồn số lượng
                redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật số lượng sản phẩm trong giỏ hàng!");
                return "redirect:/user/home";
            }
        }

        // Nếu chưa có thì thêm mới vào giỏ
        CartItem newItem = new CartItem(product, user, quantity);
        cart.add(newItem);

        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng!");
        return null;
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}

