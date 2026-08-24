package com.janickmediadb.janickmediaapi.model.response;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GenreSearchCriteria implements Serializable {

    private int id;
    private String name;
}
