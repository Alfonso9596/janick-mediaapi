package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
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
@Table(name = RecipeRatingXrefEntity.RECIPE_RATING_TABLE_NAME)
public class RecipeRatingXrefEntity extends AbstractEntity {

    static final String RECIPE_RATING_TABLE_NAME = "recipe_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = RecipeEntity.class)
    @JoinColumn(name = "RECIPE_ID")
    private RecipeEntity recipe;

    @ManyToOne(targetEntity = UsersEntity.class)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    private int rating;
}
