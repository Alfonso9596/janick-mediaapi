package com.janickmediadb.janickmediaapi.model.response;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GameSearchCriteria implements Serializable {

    private int id;
    private String name;
    private int year;
    private String genre;
    private String platform;
}
