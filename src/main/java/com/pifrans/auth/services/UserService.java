package com.pifrans.auth.services;

import com.pifrans.auth.securities.UserDetailsSecurity;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            String message = String.format("Usuário com e-mail (%s) não encontrado!", email);
            throw new UsernameNotFoundException(message);
        }

        List<SimpleGrantedAuthority> authorities = user.getProfiles().stream().map(x -> new SimpleGrantedAuthority(x.getPermission())).collect(Collectors.toList());
        return new UserDetailsSecurity(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }
}
