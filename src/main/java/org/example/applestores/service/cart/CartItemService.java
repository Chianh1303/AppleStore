package org.example.applestores.service.cart;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.OrderSession;
import org.example.applestores.model.Product;
import org.example.applestores.model.User;
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

//
//    @Override
//    public void save(CartItem cartItem) {
//        cartItemRepository.save(cartItem);
//    }
//
    @Override
    public List<CartItem> findByUser(HttpSession session, Model model) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cart");
        if(list == null) {
            return null;
        }
        User user = (User) session.getAttribute("loggedInUser");
        List<CartItem> itemList = new ArrayList<>();
        for (CartItem item : list) {
            if(item.getUser() == user) {
                itemList.add(item);
            }
        }
        return list;
    }

    @Override
    public void removeById(Long id, HttpSession session, Model model) {
        findByUser(session,model).removeIf(item -> item.getProduct().getId() == id);
    }

//    @Override
//    public void checkoutAndStoreInSession(HttpSession session) {
//        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) return;
//
//        List<CartItem> cartItems =findByUser(session);
//        if (cartItems.isEmpty()) return;
//
//        long total = cartItems.stream()
//                .mapToLong(item -> (long) (item.getQuantity() * item.getProduct().getPrice()))
//                .sum();
//
//        OrderSession orderSession = new OrderSession(cartItems, total, LocalDateTime.now());
//
//        session.setAttribute("orderSession", orderSession); // Lưu đơn hàng vào session
//
//        cartItemRepository.deleteAll(cartItems); // Xóa giỏ hàng
//    }
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
            CartItem newItem = new CartItem(product, quantity,user);
            cart.add(newItem);

            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng!");
            return null;
        }

    }

