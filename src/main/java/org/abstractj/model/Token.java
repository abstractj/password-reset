package org.abstractj.model;

import org.abstractj.api.ExpirationTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {

    @Id
    private String id;
    private Long sentAt;
    private Long expiration;
    private Boolean used = false;

    public Token() {
    }

    public Token(String id, ExpirationTime expirationTime) {
        this.id = id;
        this.sentAt = expirationTime.getCurrentTime();
        this.expiration = expirationTime.add(1);
    }

    public Token(String id) {
        this(id, new ExpirationTime());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSentAt() {
        return sentAt;
    }

    public void setSentAt(Long sentAt) {
        this.sentAt = sentAt;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = true;
    }
}
