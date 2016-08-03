package com.javaclasses.todolist.model.entity;


import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

/**
 * Security token entity implementation
 */
public class SecurityToken implements Entity<SecurityTokenId> {

    private SecurityTokenId tokenId;
    private UserId userId;

    public SecurityToken(UserId userId) {
        this.userId = userId;
    }


    @Override
    public SecurityTokenId getId() {
        return tokenId;
    }

    @Override
    public void setId(SecurityTokenId id) {
        this.tokenId = id;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityToken that = (SecurityToken) o;

        if (!tokenId.equals(that.tokenId)) return false;
        return userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
