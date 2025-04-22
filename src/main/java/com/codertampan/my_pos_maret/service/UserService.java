package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.AuthLog;
import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.entity.UserRole;
import com.codertampan.my_pos_maret.repository.AuthLogRepository;
import com.codertampan.my_pos_maret.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthLogRepository authLogRepository;

    public boolean validateLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
    
        boolean success = false;
        String action = "LOGIN_FAILED";
    
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            success = true;
            action = "LOGIN_SUCCESS";
        }
    
        AuthLog log = new AuthLog();
        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress("127.0.0.1");
        log.setUserAgent("Unknown Device");

        authLogRepository.save(log);
    
        return success;
    }

    public boolean createUser(String username, String password, String adminPassword) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }
    
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setCreatedAt(LocalDateTime.now());
    
        if ("haloadmin".equals(adminPassword)) {
            newUser.setRole(UserRole.ADMIN);
        } else {
            newUser.setRole(UserRole.KASIR);
        }
    
        AuthLog log = new AuthLog();
        log.setUsername(username);
        log.setAction("SIGNUP_SUCCESS");
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress("127.0.0.1");
        log.setUserAgent("Unknown Device");
    
        try {
            userRepository.save(newUser);
            authLogRepository.save(log);
            System.out.println("✅ User & log saved successfully");
        } catch (Exception e) {
            System.out.println("❌ Error while saving user or log: " + e.getMessage());
            throw e;
        }
    
        return true;
    }
    

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
