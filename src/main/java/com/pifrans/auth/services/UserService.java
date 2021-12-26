package com.pifrans.auth.services;

import com.pifrans.auth.models.Profile;
import com.pifrans.auth.securities.UserDetailsSecurity;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends GenericService<User> implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(JpaRepository<User, Long> repository, UserRepository userRepository, ProfileService profileService, BCryptPasswordEncoder encoder) {
        super(repository);
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.encoder = encoder;
    }

    public static UserDetailsSecurity userLogged() {
        try {
            return (UserDetailsSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            String message = String.format("Usuário com e-mail (%s) não encontrado!", email);
            throw new UsernameNotFoundException(message);
        }

        List<SimpleGrantedAuthority> authorities = user.getProfiles().stream().map(x -> new SimpleGrantedAuthority(x.getPermission().name())).collect(Collectors.toList());
        return new UserDetailsSecurity(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User save(User object) {
        this.addProfileUser(object);
        object.setActive(true);
        object.setPassword(encoder.encode(object.getPassword()));
        return super.save(object);
    }

    @Override
    public List<User> saveAll(List<User> list) {
        for (User object : list) {
            this.addProfileUser(object);
            object.setActive(true);
            object.setPassword(encoder.encode(object.getPassword()));
        }
        return super.saveAll(list);
    }

    @Override
    public User update(User object, Long id) {
        object.setPassword(encoder.encode(object.getPassword()));
        return super.update(object, id);
    }

    private void addProfileUser(User object) {
        if (object.getProfiles() == null) {
            object.setProfiles(new HashSet<>(Set.of(profileService.findById(Profile.class, 2L))));
        } else if (object.getProfiles().size() < 1) {
            object.setProfiles(Set.of(profileService.findById(Profile.class, 2L)));
        }
    }
}
