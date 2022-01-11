package com.pifrans.auth.listeners;

import com.pifrans.auth.models.User;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.util.logging.Logger;

@Configuration
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private static final Logger LOG = Logger.getLogger(LoginListener.class.getName());
    private final UserService userService;

    @Autowired
    public LoginListener(UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            User user = userService.findByEmail(event.getAuthentication().getName());
            user = userService.updateAccessDates(user);

            LOG.info(String.format("Usuário (%s) logado com sucesso!", user.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
