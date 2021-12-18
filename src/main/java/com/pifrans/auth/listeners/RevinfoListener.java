package com.pifrans.auth.listeners;

import com.pifrans.auth.models.Revinfo;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevinfoListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        Revinfo revinfo = (Revinfo) o;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        revinfo.setUsername((authentication != null) ? authentication.getName() : "Start application");
    }
}
