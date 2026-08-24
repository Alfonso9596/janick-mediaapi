package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MealTypeEntity;
import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
