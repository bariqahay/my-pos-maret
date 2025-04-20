package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.service.UserSession;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SecureView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserSession userSession;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (userSession == null || !userSession.isLoggedIn()) {
            // Gak ada user login → tendang balik ke login
            event.forwardTo("");
        }
    }
}
