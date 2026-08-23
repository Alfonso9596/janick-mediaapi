package com.janick_mediadb.janick_mediaapi.input;

import com.janick_mediadb.janick_mediaapi.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeInput extends AbstractModel {

    private String name;
    private String description;
    private List<String> mealTypes;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenfree;
    private boolean lactosefree;
}
