package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = RecipeMealTypeXrefEntity.RECIPE_MEAL_TYPE_TABLE_NAME)
public class RecipeMealTypeXrefEntity extends AbstractEntity {

    static final String RECIPE_MEAL_TYPE_TABLE_NAME = "recipe_meal_type_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = RecipeEntity.class)
    @JoinColumn(name = "RECIPE_ID")
    private RecipeEntity recipe;

    @ManyToOne(targetEntity = MealTypeEntity.class)
    @JoinColumn(name = "MEAL_TYPE_ID")
    private MealTypeEntity mealType;
}
