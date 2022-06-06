package com.example.paintballclub.services;

import com.example.paintballclub.dto.OrderDTO;
import com.example.paintballclub.model.Order;
import com.example.paintballclub.model.User;
import com.example.paintballclub.repositories.OrderRepository;
import com.example.paintballclub.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Order save(OrderDTO orderDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Order order = new Order(orderDTO.getKit(),orderDTO.getDate(),orderDTO.getTime(), orderDTO.getAddress(), orderDTO.getWishes(), user);
        try {
            String text = "Спасибо за заказ на нашем сайте! \nВы заказали " + orderDTO.getKit() + ". Ожидайте звонка оператора.";
            emailService.sendSimpleMessage("Оформление доставки", text, email);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderRepository.save(order);
    }
}
