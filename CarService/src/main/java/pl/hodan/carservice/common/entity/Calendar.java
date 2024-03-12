package pl.hodan.carservice.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.hodan.carservice.common.messages.Messages;

import java.sql.Date;

@Data
@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = Messages.DATE_IS_NULL)
    private Date dateEvent;
    @NotBlank(message = Messages.EMPTY_FIELD)
    @Size(min = 4, message = Messages.WRONG_LENGTH_TEXT)
    private String event;
    @JsonIgnore
    @ManyToOne
    private User user;


    public Calendar() {
    }
}
