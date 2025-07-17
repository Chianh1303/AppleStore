package org.example.applestores.service.cart;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.Order;
import org.example.applestores.model.Product;
import org.example.applestores.model.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;
import java.util.List;

public interface ICartItemService {
    List<CartItem> findCartByUser(HttpSession session, Model model);
    void removeById(Long id, HttpSession session, Model model);
//    void checkoutAndStoreInSession(HttpSession session) ;
void checkoutAllFromSession(HttpSession session);
String addToCart(Product product, int quantity, HttpSession session, RedirectAttributes redirectAttributes);

    List<Order> findByUser(User user);
}
