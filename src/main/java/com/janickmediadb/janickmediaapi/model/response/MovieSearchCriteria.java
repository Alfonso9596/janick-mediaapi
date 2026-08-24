package com.janickmediadb.janickmediaapi.model.response;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MovieSearchCriteria implements Serializable {

    private int id;
    private String name;
    private int year = 0;
    private String genre;
}
