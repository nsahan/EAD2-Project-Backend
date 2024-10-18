package com.example.emaillogin.Service;


import com.example.emaillogin.Model.User;
import com.example.emaillogin.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Password encryption
        userRepository.save(user);

        // Send registration email
        String subject = "Registration Successful";
        String message = "Hello " + user.getName() + ",\n\nWelcome to Shop Cart! Your registration is successful!\n \n"
                +"Congratulations and welcome to Shop Cart.We’re excited to have you as a member of our shoe-loving community.You can now start exploring our wide range of stylish and comfortable footwear.\n" +
                "\n \n" +
                "Best Regards, \n "
                +
                "Shop Cart Team"
                ;
        emailService.sendRegistrationEmail(user.getEmail(), subject, message);

        return user;
    }
    public boolean authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User foundUser = user.get();
            return passwordEncoder.matches(password, foundUser.getPassword());
        }
        return false;
    }


}
