package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.entity.UserRole;
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

    public boolean validateLogin(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("[LOGIN FAILED] User not found: " + username);
            return false;
        }

        boolean passwordMatch = BCrypt.checkpw(password, user.getPasswordHash());
        if (!passwordMatch) {
            System.out.println("[LOGIN FAILED] Invalid password for user: " + username);
        }

        return passwordMatch;
    }

    /**
     * Create a new user with given role based on admin password.
     * If adminPassword is correct ("haloadmin"), user will be ADMIN.
     * Otherwise, default role is KASIR.
     */
    public boolean createUser(String username, String plainPassword, String adminPassword) {
        if (userRepository.findByUsername(username) != null) {
            System.out.println("[SIGNUP FAILED] Username already taken: " + username);
            return false;
        }

        UserRole role = UserRole.KASIR;
        if ("haloadmin".equals(adminPassword)) {
            role = UserRole.ADMIN;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
        newUser.setRole(role);
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.save(newUser);
        System.out.println("[SIGNUP SUCCESS] User created: " + username + " as " + role);
        return true;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Optional: still provide raw save for internal purposes (but NEVER call this directly from UI)
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
