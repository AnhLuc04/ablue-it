//package com.example.ablueit.model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//
//public class User  implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String username;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//    @Column(nullable = false)
//    private String secretKey;
//
//    private boolean enabled = true;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>(); // Lưu role dưới dạng Set<Role>
//
//
//    // Một người dùng có thể thuộc một cửa hàng (Nếu là ADMIN thì store = null)
//    @ManyToOne
//    @JoinColumn(name = "store_id")
//    private Store store;
//
//    public User() {
//
//    }
//
//    public User(String username, String email, String password, String secretKey, boolean enabled, Set<Role> roles) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.secretKey = secretKey;
//        this.enabled = enabled;
//        this.roles = roles;
//    }
//
//    public User(Long id, String username, String email, String password, String secretKey, boolean enabled, Set<Role> roles) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.secretKey = secretKey;
//        this.enabled = enabled;
//        this.roles = roles;
//    }
//
//    public User(String username, String email, String password, String secretKey, Set<Role> roles, Store store) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.secretKey = secretKey;
//        this.roles = roles;
//        this.store = store;
//    }
//
//    public User(Long id, String username, String email, String password, String secretKey, Set<Role> roles, Store store) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.secretKey = secretKey;
//        this.roles = roles;
//        this.store = store;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getSecretKey() {
//        return secretKey;
//    }
//
//    public void setSecretKey(String secretKey) {
//        this.secretKey = secretKey;
//    }
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//
//    public Store getStore() {
//        return store;
//    }
//
//    public void setStore(Store store) {
//        this.store = store;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }
//}









package com.example.ablueit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String secretKey;

    private boolean enabled = true;

    // Một người dùng có thể có nhiều role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Một người dùng có thể thuộc một cửa hàng (Nếu là ADMIN thì store = null)
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    // Người tạo tài khoản này
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public User() {
    }

    public User(String username, String email, String password, String secretKey, boolean enabled, Set<Role> roles, Store store, User createdBy) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.secretKey = secretKey;
        this.enabled = enabled;
        this.roles = roles;
        this.store = store;
        this.createdBy = createdBy;
    }

    // ✅ Lấy danh sách quyền (Authorities) từ danh sách Role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Mặc định tài khoản không hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Mặc định tài khoản không bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mật khẩu không hết hạn
    }

    @Override
    public boolean isEnabled() {
        return enabled; // Trả về trạng thái enabled của user
    }
}
