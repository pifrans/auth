package com.pifrans.auth.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "records", catalog = "auth", schema = "auth")
public class Record implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "creation_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date creationDate;

    @Column(name = "change_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date changeDate;
}
