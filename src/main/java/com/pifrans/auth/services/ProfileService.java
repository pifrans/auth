package com.pifrans.auth.services;

import com.pifrans.auth.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService extends GenericService<Profile> {

    @Autowired
    public ProfileService(JpaRepository<Profile, Long> repository) {
        super(repository);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Profile findById(Class<Profile> profileClass, Long id) {
        return super.findById(profileClass, id);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public List<Profile> findAll() {
        return super.findAll();
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Page<Profile> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        return super.findByPage(page, linesPerPage, orderBy, direction);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Profile save(Profile object) throws DataIntegrityViolationException {
        return super.save(object);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public List<Profile> saveAll(List<Profile> list) throws DataIntegrityViolationException {
        return super.saveAll(list);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Profile update(Profile object, Long id) throws DataIntegrityViolationException {
        return super.update(object, id);
    }

    @PreAuthorize("hasAnyRole(@userGroups.groupAdmin())")
    @Override
    public Profile deleteById(Class<Profile> profileClass, Long id) {
        return super.deleteById(profileClass, id);
    }
}
