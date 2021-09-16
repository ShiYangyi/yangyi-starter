package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;

    public static ParkingLotBuilder builder() {
        return new ParkingLotBuilder();
    }

    @Override
    public String toString() {
        //Request{name='syy', password='1'},这是原来toString()的格式
        //{"name": "po", "password": "0"}，重写后的格式
        String result = "{\"name\": \"" + name + '\"' + ", \"username\": \"" + username + '\"' + '}';
        return result;
    }

    public static class ParkingLotBuilder {
        private BigInteger id;
        private String name;
        private String username;

        ParkingLotBuilder() {
        }

        public ParkingLotBuilder id(BigInteger id) {
            this.id = id;
            return this;
        }

        public ParkingLotBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ParkingLotBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ParkingLot build() {
            return new ParkingLot(id, name, username);
        }

        /*public String toString() {
            return "ParkingLot.ParkingLotBuilder(id=" + this.id + ", name=" + this.name + ", username=" + this.username + ")";
        }*/
    }
}
