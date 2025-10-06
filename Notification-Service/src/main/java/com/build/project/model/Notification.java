package com.build.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationDTO {
    @Id
    private UUID id = UUID.randomUUID();

    private String recipientId;
    private String type;
    private String title;

    @Column(columnDefinition = "text")
    private String body;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private String status;

    private Instant createdAt = Instant.now();
    private Instant deliveredAt;
    private int attempts = 0;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
