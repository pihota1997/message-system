package com.inside.messagesystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class UpdatableEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updateAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        updateAt = Instant.now();
    }
}
