package com.cloudcomputing.csye6225.controller;

import com.cloudcomputing.csye6225.model.User;
import com.cloudcomputing.csye6225.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/v5/user")
    public ResponseEntity<?> createUserApi(@RequestBody User user, HttpServletRequest request) throws Exception {
        return userService.createNewUser(user, request);
    }

    @GetMapping(path = "/v5/user/self")
    public ResponseEntity<?> getUserDetailsApi(HttpServletRequest request) throws Exception {
        return userService.getUserDetails(request);
    }

    @PutMapping(path = "/v5/user/self")
    public ResponseEntity<?> updateUserApi(@RequestBody User user, HttpServletRequest request) throws Exception {
        return userService.updateUserDetails(user, request);
    }

    @GetMapping(path = "/v5/user/verification")
    public ResponseEntity<?> getUserVerificationApi(HttpServletRequest request) throws Exception {
        return userService.getUserVerificationInformation(request);
    }
}
