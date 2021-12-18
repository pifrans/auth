package com.pifrans.auth.models;

import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditTable(value = "aud_profiles", catalog = "audit", schema = "audit")
@Entity
@Table(name = "profiles", catalog = "auth", schema = "auth")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;
}