package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.dto.cart.CartDto;
import com.mazbah.ecomd.dto.cart.CartItemDto;
import com.mazbah.ecomd.dto.checkout.CheckOutItemDto;
import com.mazbah.ecomd.entity.Order;
import com.mazbah.ecomd.entity.OrderItem;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.OrderNotFoundException;
import com.mazbah.ecomd.repository.OrderItemRepository;
import com.mazbah.ecomd.repository.OrderRepository;
import com.mazbah.ecomd.service.CartService;
import com.mazbah.ecomd.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

//    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    // Create Total price
    SessionCreateParams.LineItem.PriceData createPriceData(CheckOutItemDto checkOutItemDto){
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("taka")
                .setUnitAmount((long)(checkOutItemDto.getPrice()))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(checkOutItemDto.getProductName())
                        .build())
                .build();
    }

    // build each product in the stripe checkout page
    SessionCreateParams.LineItem createSessionLineItem(CheckOutItemDto checkOutItemDto){
        return SessionCreateParams.LineItem.builder()
                // set price for each product
                .setPriceData(createPriceData(checkOutItemDto))
                // set quantity for each product
                .setQuantity(Long.parseLong(String.valueOf(checkOutItemDto.getQuantity())))
                .build();
    }

    // create session from list of checkout items
    public Session createSession(List<CheckOutItemDto> checkOutItemDtoList) throws StripeException{

        // supply success and failure url for stripe
        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";

        // set the private key
        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for(CheckOutItemDto checkOutItemDto: checkOutItemDtoList){
            sessionItemsList.add(createSessionLineItem(checkOutItemDto));
        }

        // build the session param
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();

        return Session.create(params);
    }

    public void placeOrder(User user, String sessionId){
        // first let get cart items for the user
        CartDto cartDto = cartService.listCartItems(user);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // create the order and save it
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        orderRepository.save(newOrder);

        for(CartItemDto cartItemDto: cartItemDtoList){
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);

            // add to order item list
            orderItemRepository.save(orderItem);
        }
        cartService.deleteUserCartItems(user);
    }

    public List<Order> listOrders(User user){ return orderRepository.findAllByUserOrderByCreatedDateDesc(user);}

    public Order getOrder(Integer orderId) throws OrderNotFoundException{
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            return order.get();
        }
        throw new OrderNotFoundException("Order Not Found!!!");
    }

}
