package ch.bzz.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Booking {

    private Integer id;
    private Integer bookingNumber;
    private LocalDate date;
    private String text;
    private Account debitAccount;
    private Account creditAccount;
    private BigDecimal amount;
    private Project project;
}
