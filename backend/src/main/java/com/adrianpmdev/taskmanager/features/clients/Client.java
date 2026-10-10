package com.adrianpmdev.taskmanager.features.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Client of the company whose tasks are tracked.
 *
 * @author adrianpmdev
 */
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Client() {
        // Required by JPA
    }

    /**
     * Creates a client.
     *
     * @param name  name
     * @param email contact email, optional
     * @param phone contact phone, optional
     */
    public Client(String name, String email, String phone) {
        update(name, email, phone);
    }

    /**
     * Replaces the editable data of the client.
     *
     * @param name  name
     * @param email contact email, optional
     * @param phone contact phone, optional
     */
    public void update(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
