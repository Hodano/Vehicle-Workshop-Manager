package pl.hodan.carservice.common.vindecoder.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({
        "VIN",
        "Region",
        "Country",
        "Make",
        "Model",
        "Model year",
        "Body style",
        "Engine type",
        "Transmission",
        "Fuel type",
        "Body type",
        "Manufactured in",
        "Number of doors",
        "Number of seats"
})
@Getter
@Setter
public class VINDecoderModel {
    @JsonProperty("VIN")
    private String VIN;
    @JsonProperty("Region")
    private String region;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Make")
    private String make;
    @JsonProperty("Model")
    private String model;
    @JsonProperty("Model year")
    private String modelYear;
    @JsonProperty("Body style")
    private String bodyStyle;
    @JsonProperty("Engine type")
    private String engineType;
    @JsonProperty("Fuel type")
    private String fuelType;
    @JsonProperty("Transmission")
    private String transmission;
    @JsonProperty("Manufactured in")
    private String manufacturedIn;
    @JsonProperty("Body type")
    private String bodyType;
    @JsonProperty("Number of doors")
    private String numberOfDoors;
    @JsonProperty("Number of seats")
    private String numberOfSeats;
    @JsonProperty("message")
    private String messages;
    @JsonProperty("success")
    private String success;



    @Override
    public String toString() {
        return "VINDecoderModel{" +
                "VIN='" + VIN + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", bodyStyle='" + bodyStyle + '\'' +
                ", engineType='" + engineType + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", manufacturedIn='" + manufacturedIn + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", numberOfDoors='" + numberOfDoors + '\'' +
                ", numberOfSeats='" + numberOfSeats + '\'' +
                ", messages='" + messages + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
