package ch.bzz.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

    private Integer id;
    private Integer accountNumber;
    private String name;
    private Project project;
}
