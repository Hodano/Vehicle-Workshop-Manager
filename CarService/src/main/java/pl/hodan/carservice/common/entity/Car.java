package pl.hodan.carservice.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String VIN;
    private String region;
    private String country;
    private String make;
    private String model;
    private String modelYear;
    private String bodyStyle;
    private String engineType;
    private String fuelType;
    private String transmission;
    private String manufacturedIn;
    private String bodyType;
    private String numberOfDoors;
    private String numberOfSeats;
    @ManyToOne
    private Client client;
    @JsonIgnore
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<History> historySet;

    public Car() {
    }

}
