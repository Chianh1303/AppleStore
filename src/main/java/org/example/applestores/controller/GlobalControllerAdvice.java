package org.example.applestores.controller;


import org.example.applestores.model.CartItem;
import org.example.applestores.model.User;
import org.example.applestores.service.cart.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ICartItemService cartItemService;

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount(HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
        if (cartItems == null) return 0;

        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @ModelAttribute
    public void addLoggedInUserToModel(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
    }
}

