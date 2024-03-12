package pl.hodan.carservice.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.hodan.carservice.common.messages.Messages;

@Data
@Entity
public class PriceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String nameOfService;
    @NotNull(message = Messages.PRICE_IS_NULL)
    @Min(value = 1, message = Messages.PRICE_MUST_BE_BIGGER_THAN_1)
    private Double prices;
    @JsonIgnore
    @ManyToOne
    private User user;


    public PriceList() {
    }
}
