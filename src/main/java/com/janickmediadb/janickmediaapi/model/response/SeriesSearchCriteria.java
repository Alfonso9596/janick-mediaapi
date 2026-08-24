package com.janickmediadb.janickmediaapi.model.response;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SeriesSearchCriteria implements Serializable {

    private int id;
    private String name;
    private int yearStart = 0;
    private int yearEnd = 0;
    private String genre;
}
