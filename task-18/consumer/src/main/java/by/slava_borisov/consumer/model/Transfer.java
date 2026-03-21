package by.slava_borisov.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    private String id;

    private Long fromAccountId;

    private Long toAccountId;

    private BigDecimal amount;

    private String status;
}
