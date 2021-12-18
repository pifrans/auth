package com.pifrans.auth.models;

import com.pifrans.auth.listeners.RevinfoListener;
import lombok.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RevisionEntity(RevinfoListener.class)
@Table(name = "revinfo", catalog = "audit", schema = "audit")
public class Revinfo {

    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @RevisionTimestamp
    private long revtstmp;

    private String username;
}
