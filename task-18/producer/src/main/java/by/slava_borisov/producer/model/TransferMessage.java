package by.slava_borisov.producer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferMessage implements Serializable {

    private String id;

    private Long fromAccountId;

    private Long toAccountId;

    private BigDecimal amount;
}
