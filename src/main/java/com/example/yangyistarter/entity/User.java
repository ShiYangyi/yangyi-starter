package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.math.BigInteger;
import java.security.Principal;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
//这个类是与数据库repository层交互的，只需要对接口（与前端）交互的类写上限制验证的注解即可，即dto文件夹下。
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "username")
    private String name;
    private String password;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    @Override
    //重写的方法返回值和形参都不能改变，这些方法没有使用到，与token无关，与token设置的过期时间也无关。
    //UserDetail这个类就是用来存储权限相关信息的，那几个重写的属于UserDetail类中的方法，
    // 为了不让其影响现有程序，所以让它们全部返回true。
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class UserBuilder {
        private BigInteger id;
        private String name;
        private String password;

        UserBuilder() {
        }

        public UserBuilder id(BigInteger id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(id, name, password);
        }

        /*public String toString() {
            return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ", password=" + this.password + ")";
        }*/
    }
}
