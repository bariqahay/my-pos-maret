package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session") // ini session-scoped bean yang aman buat dipakai di Vaadin modern
public class UserSession {

    private User currentUser;

    public User getUser() {
        return currentUser;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void clear() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

