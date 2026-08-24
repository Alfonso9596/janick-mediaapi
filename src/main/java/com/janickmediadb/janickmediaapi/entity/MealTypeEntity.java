package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.input.MealTypeInput;
import com.janickmediadb.janickmediaapi.model.MealTypeModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
