package org.example.applestores.service.cart;

import org.example.applestores.model.*;
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

    @Override
    public List<CartItem> findCartByUser(HttpSession session, Model model) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cart");
        if (list == null) return null;

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return null;

        List<CartItem> itemList = new ArrayList<>();
        for (CartItem item : list) {
            if (item.getUser() != null && user.getId().equals(item.getUser().getId())) {
                itemList.add(item);
            }
        }
        return itemList;
    }


    @Override
    public void removeById(Long id, HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getProduct().getId().equals(id));
            session.setAttribute("cart", cart);
            model.addAttribute("cart", cart);
        }
    }


    @Override
    public String addToCart(Product product, int quantity, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");

        if (product.getStock() < quantity) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not enough stock available.");
            return "redirect:/user/home"; // or redirect to product detail page
        }

        // Get cart from session, create new if not exist
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                int totalQuantity = item.getQuantity() + quantity;

                if (totalQuantity > product.getStock()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Cannot add more than available stock.");
                    return "redirect:/user/home";
                }

                item.setQuantity(totalQuantity);
                redirectAttributes.addFlashAttribute("successMessage", "Product quantity updated in cart.");
                return "redirect:/user/home";
            }
        }

        // If product not in cart, add new item
        CartItem newItem = new CartItem(product, user, quantity);
        cart.add(newItem);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart.");
        return null;
    }



    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}

