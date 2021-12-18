package com.pifrans.auth.services;

import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> saveAll(List<User> users) {
        try {
            return userRepository.saveAll(users);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User update(User object) throws NoSuchFieldException {
        if (userRepository.existsById(object.getId())) {
            return userRepository.save(object);
        } else {
            String message = String.format("Erro ao atualizar usuário de ID (%d), não encontrado!", object.getId());
            throw new NoSuchFieldException(message);
        }
    }
}
