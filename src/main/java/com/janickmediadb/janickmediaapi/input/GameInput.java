package com.janickmediadb.janickmediaapi.input;

import com.janickmediadb.janickmediaapi.model.AbstractModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameInput extends AbstractModel {

    private String name;
    private String description;
    private String year;
    private List<String> genres;
    private List<String> platforms;
}
