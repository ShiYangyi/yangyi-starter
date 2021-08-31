package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//为什么把注解delombok后，这个注解注释掉还是会出错？不是已经产生了等价的方法了吗？？？？？？？？？？？？
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "username")
    private String name;
    private String password;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private BigInteger id;
        private String name;
        private String password;

        UserBuilder() {
        }

        /*public UserBuilder id(BigInteger id) {
            this.id = id;
            return this;
        }*/

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
