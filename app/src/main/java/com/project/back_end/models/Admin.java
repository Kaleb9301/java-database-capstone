package com.project.back_end.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GenerationType;

@Entity
public class Admin {

@Id
@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
private Long id;

@NotNull
private String username;


@NotNull
private String password;


public Admin(Long id, String username, String password) {
    // Default constructor required by JPA
    this.id = id;
    this.username = username;   
    this.password = password;
}


public Long getId() {
    return id;

}
public void setId(Long id) {
    this.id = id;
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
@Override
public String toString() {
    return "Admin{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
}

@Override
public boolean equals(Object o) {                                               
    if (this == o) return true;
    if (!(o instanceof Admin)) return false;

    Admin admin = (Admin) o;

    if (!id.equals(admin.id)) return false;
    if (!username.equals(admin.username)) return false;
    return password.equals(admin.password);
}

@Override
public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + username.hashCode();
    result = 31 * result + password.hashCode();
    return result;                              

}

// 6. Additional Notes:
//    - The @Entity annotation indicates that this class is a JPA entity.                                                                                                                        


}