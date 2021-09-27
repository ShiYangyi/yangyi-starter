package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;

    public static ParkingLotBuilder builder() {
        return new ParkingLotBuilder();
    }

    public static class ParkingLotBuilder {
        private Long id;
        private String name;
        private String username;

        ParkingLotBuilder() {
        }

        public ParkingLotBuilder id(long id) {
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
    }
}
