package com.pifrans.auth.services;

import com.pifrans.auth.dtos.users.UserUpdatePasswordDTO;
import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import com.pifrans.auth.securities.UserDetailsSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public UserDetailsSecurity userLogged() {
        try {
            return (UserDetailsSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.findByEmail(email);
        List<SimpleGrantedAuthority> authorities = user.getProfiles().stream().map(x -> new SimpleGrantedAuthority(x.getPermission().name())).collect(Collectors.toList());
        return new UserDetailsSecurity(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    public User findByEmail(String email) {
        String message = String.format("Usuário com e-mail (%s) não encontrado!", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(message));
    }

    @Override
    public User save(User object) throws DataIntegrityViolationException {
        User user = User.builder().name(object.getName()).email(object.getEmail()).password(encoder.encode(object.getPassword())).isActive(true).profiles(this.addProfileUser(object)).build();
        return super.save(user);
    }

    @Override
    public List<User> saveAll(List<User> list) throws DataIntegrityViolationException {
        for (User object : list) {
            object.setProfiles(this.addProfileUser(object));
            object.setCurrentAccess(null);
            object.setLastAccess(null);
            object.setActive(true);
            object.setToken(null);
            object.setPassword(encoder.encode(object.getPassword()));
        }
        return super.saveAll(list);
    }

    public User updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        User object = super.findById(User.class, userUpdatePasswordDTO.getId());
        object.setPassword(encoder.encode(userUpdatePasswordDTO.getPassword()));
        return super.update(object, object.getId());
    }

    public User updateToken(String token, Long id) {
        User object = super.findById(User.class, id);
        object.setToken(token);
        return super.update(object, id);
    }

    private Set<Profile> addProfileUser(User object) {
        Set<Profile> profiles = new HashSet<>();
        if (object.getProfiles() == null) {
            profiles = Set.of(profileService.findById(Profile.class, 2L));
        } else if (object.getProfiles().size() < 1) {
            profiles = Set.of(profileService.findById(Profile.class, 2L));
        }
        return profiles;
    }
}
