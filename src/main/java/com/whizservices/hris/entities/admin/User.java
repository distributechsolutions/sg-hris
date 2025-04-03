package com.whizservices.hris.entities.admin;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;
import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_user_account")
public class User extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true, nullable = false)
    private Employee employee;

    @Column(name = "username", length = 25, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 250, nullable = false)
    private String password;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    @Column(name = "email_address", length = 50, nullable = false)
    private String emailAddress;

    @Column(name = "is_account_active", nullable = false)
    private boolean accountActive;

    @Column(name = "is_password_changed", nullable = false)
    private boolean passwordChanged;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}
