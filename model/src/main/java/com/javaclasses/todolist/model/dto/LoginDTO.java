package com.javaclasses.todolist.model.dto;

/**
 * Data transfer object of user login information
 */
public class LoginDTO {

    private final String email;
    private final String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginDTO loginDTO = (LoginDTO) o;

        if (!email.equals(loginDTO.email)) return false;
        return password.equals(loginDTO.password);

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
