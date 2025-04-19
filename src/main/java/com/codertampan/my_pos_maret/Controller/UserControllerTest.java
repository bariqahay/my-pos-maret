package com.codertampan.my_pos_maret.Controller;

import com.codertampan.my_pos_maret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pingdb")
    public String pingDB() {
        // Cek kalo data bisa diambil dari DB
        long userCount = userRepository.count();
        return "Jumlah user di DB: " + userCount;
    }
}
