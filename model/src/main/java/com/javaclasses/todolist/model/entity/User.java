package com.javaclasses.todolist.model.entity;

import com.javaclasses.todolist.model.entity.tinytype.Email;
import com.javaclasses.todolist.model.entity.tinytype.Password;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

/**
 * User entity implementation
 */
public class User implements Entity<UserId> {

    private UserId userId;
    private Email email;
    private Password password;

    @Override
    public UserId getId() {
        return userId;
    }

    @Override
    public void setId(UserId id) {
        this.userId = id;
    }

    public User(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
