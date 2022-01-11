package com.pifrans.auth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pifrans.auth.constants.UserProfiles;
import com.pifrans.auth.dtos.users.UserUpdatePasswordDTO;
import com.pifrans.auth.dtos.users.UserUpdateProfilesdDTO;
import com.pifrans.auth.dtos.users.UserUpdateSimpleDataDTO;
import com.pifrans.auth.exceptions.errors.PermissionException;
import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import com.pifrans.auth.securities.UserDetailsSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends GenericService<User> implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(JpaRepository<User, Long> repository, UserRepository userRepository, ProfileService profileService, ObjectMapper objectMapper) {
        super(repository);
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.objectMapper = objectMapper;
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
        List<SimpleGrantedAuthority> authorities = user.getProfiles().stream().map(x -> new SimpleGrantedAuthority(x.getPermission())).collect(Collectors.toList());
        return new UserDetailsSecurity(user.getId(), user.getEmail(), user.getPassword(), user.isActive(), authorities);
    }

    @Override
    public User findById(Class<User> userClass, Long id) {
        if (this.checkPermissionAndAccess(id)) {
            return super.findById(userClass, id);
        }
        String message = String.format("O usuário logado não tem permissão para acessar o usuário de ID (%d)!", id);
        throw new PermissionException(message);
    }

    public User findByEmail(String email) {
        String message = String.format("Usuário com e-mail (%s) não encontrado!", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(message));
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Page<User> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        return super.findByPage(page, linesPerPage, orderBy, direction);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public User save(User object) throws DataIntegrityViolationException {
        User user = new User();
        user.setName(object.getName());
        user.setEmail(object.getEmail());
        user.setPassword(object.getPassword());
        user.setActive(true);
        user.setProfiles(this.addProfileUser(object));
        return super.save(user);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public List<User> saveAll(List<User> list) throws DataIntegrityViolationException {
        for (User object : list) {
            object.setProfiles(this.addProfileUser(object));
            object.setCurrentAccess(null);
            object.setLastAccess(null);
            object.setActive(true);
            object.setToken(null);
            object.setPassword(object.getPassword());
        }
        return super.saveAll(list);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public User update(User object, Long id) throws DataIntegrityViolationException {
        return super.update(object, id);
    }

    public User updateAccessDates(User object) throws DataIntegrityViolationException {
        String message = String.format("Não foi possível atualizar datas de acesso em (%s) de ID (%d), não encontrado!", User.class.getSimpleName(), object.getId());
        object = userRepository.findById(object.getId()).orElseThrow(() -> new NoSuchElementException(message));
        object.setLastAccess(object.getCurrentAccess());
        object.setCurrentAccess(new Date(System.currentTimeMillis()));
        return userRepository.save(object);
    }

    public User updateSimpleData(UserUpdateSimpleDataDTO userUpdateSimpleDataDTO) throws DataIntegrityViolationException, JsonProcessingException {
        if (this.checkPermissionAndAccess(userUpdateSimpleDataDTO.getId())) {
            String objectJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(userUpdateSimpleDataDTO);
            User objectOld = super.findById(User.class, userUpdateSimpleDataDTO.getId());
            User objectNew = objectMapper.readerForUpdating(objectOld).readValue(objectJson);
            return super.update(objectNew, objectNew.getId());
        }
        String message = String.format("O usuário logado não tem permissão para alterar dados do usuário de ID (%d)!", userUpdateSimpleDataDTO.getId());
        throw new PermissionException(message);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    public User updateActive(Long id, Boolean isActive) {
        if (this.checkPermissionAndAccess(id)) {
            User object = super.findById(User.class, id);
            object.setActive(isActive);
            return super.update(object, object.getId());
        }
        String message = String.format("O usuário logado não tem permissão para ativar ou inativar o usuário de ID (%d)!", id);
        throw new PermissionException(message);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    public User updateProfiles(UserUpdateProfilesdDTO userUpdateProfilesdDTO) {
        if (this.checkPermissionAndAccess(userUpdateProfilesdDTO.getId())) {
            User object = super.findById(User.class, userUpdateProfilesdDTO.getId());
            object.setProfiles(userUpdateProfilesdDTO.getProfiles());
            return super.update(object, object.getId());
        }
        String message = String.format("O usuário logado não tem permissão para alterar perfis do usuário de ID (%d)!", userUpdateProfilesdDTO.getId());
        throw new PermissionException(message);
    }

    public User updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        if (this.checkPermissionAndAccess(userUpdatePasswordDTO.getId())) {
            User object = super.findById(User.class, userUpdatePasswordDTO.getId());
            object.setPassword(userUpdatePasswordDTO.getPassword());
            return super.update(object, object.getId());
        }
        String message = String.format("O usuário logado não tem permissão para alterar a senha do usuário de ID (%d)!", userUpdatePasswordDTO.getId());
        throw new PermissionException(message);
    }

    public User updateToken(String token, Long id) {
        User object = super.findById(User.class, id);
        object.setToken(token);
        return super.update(object, id);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public User deleteById(Class<User> userClass, Long id) {
        return super.deleteById(userClass, id);
    }

    private Boolean checkPermissionAndAccess(Long id) {
        Set<String> profiles = this.userLogged().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return profiles.contains(UserProfiles.ROLE_ADMIN.name()) || this.userLogged().getId().equals(id);
    }

    private Set<Profile> addProfileUser(User object) {
        Set<Profile> profiles = object.getProfiles();
        if (profiles == null) {
            profiles = Set.of(profileService.findById(Profile.class, 2L));
        } else if (profiles.size() < 1) {
            profiles = Set.of(profileService.findById(Profile.class, 2L));
        }
        return profiles;
    }
}
