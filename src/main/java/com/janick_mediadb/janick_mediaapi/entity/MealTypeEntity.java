package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.input.MealTypeInput;
import com.janick_mediadb.janick_mediaapi.model.MealTypeModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = MealTypeEntity.MEAL_TYPE_TABLE_NAME)
public class MealTypeEntity extends AbstractEntity {

    static final String MEAL_TYPE_TABLE_NAME = "meal_type";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public void fromInput(MealTypeInput mealTypeInput) {
        this.name = mealTypeInput.getName();
    }

    public MealTypeModel toModel() {
        MealTypeModel model = new MealTypeModel();
        model.setId(id);
        model.setName(name);
        return model;
    }

    public static List<String> toModels(List<MealTypeEntity> mealTypes) {
        return mealTypes.stream().map(MealTypeEntity::getName).toList();
    }
}
