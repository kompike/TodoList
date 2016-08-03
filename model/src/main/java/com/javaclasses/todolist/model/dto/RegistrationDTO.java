package com.javaclasses.todolist.model.dto;

/**
 * Data transfer object of user registration information
 */
public class RegistrationDTO {

    private final String email;
    private final String password;
    private final String confirmPassword;

    public RegistrationDTO(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationDTO that = (RegistrationDTO) o;

        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        return confirmPassword.equals(that.confirmPassword);

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + confirmPassword.hashCode();
        return result;
    }
}
