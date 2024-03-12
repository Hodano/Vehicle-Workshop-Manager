package pl.hodan.carservice.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.hodan.carservice.common.messages.Messages;

import java.util.Date;

@Data
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOfHistoryCar; // zmiana na date...
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String descriptionHistory;
    @JsonIgnore
    @ManyToOne
    private Car car;


    public History() {
    }
}
