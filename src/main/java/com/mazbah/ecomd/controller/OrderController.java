package com.mazbah.ecomd.controller;

import com.mazbah.ecomd.common.ApiResponse;
import com.mazbah.ecomd.dto.checkout.CheckOutItemDto;
import com.mazbah.ecomd.dto.checkout.StripeResponse;
import com.mazbah.ecomd.entity.Order;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.exceptions.OrderNotFoundException;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired AuthenticationService authenticationService;

    // Stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckOutItemDto> checkOutItemDtoList) throws StripeException{
        // create the stripe session
        Session session = orderService.createSession(checkOutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe sessionId in response
        return new ResponseEntity<StripeResponse>(stripeResponse, HttpStatus.OK);
    }

    //place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionID") String sessionID)
        throws AuthenticationFailException{
        // Authenticate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);

        // place the order
        orderService.placeOrder(user, sessionID);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }

    // Get all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token){
        // Authenticate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);

        List<Order> orderDtoList = orderService.listOrders(user);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // get orderitems from a order
    @PostMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
        throws AuthenticationFailException{

        // Authenticate token
        authenticationService.authenticate(token);

        try{
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }catch (OrderNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
