package com.heylee.demo.agency_service.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account", schema = "producer")
public class Account {
    @Id
    @SequenceGenerator(name = "ACCOUNT_ID_SEQUENCE", sequenceName = "producer.account_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_ID_SEQUENCE")
    private Long id;

    @Column(name = "account")
    private String account;

    @Column(name = "status")
    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
