package com.janick_mediadb.janick_mediaapi.model.specifications;

import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeMealTypeXrefEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {

    private RecipeSpecification() {
        throw new IllegalStateException("Non-constructor class");
    }

    public static Specification<RecipeEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<RecipeEntity> containsMealType(String mealType) {
        return (root, query, criteriaBuilder) -> {
            Join<RecipeEntity, RecipeMealTypeXrefEntity> recipeMealTypeXrefJoin = root.join("recipeMealTypeXrefs", JoinType.INNER);
            Join<RecipeMealTypeXrefEntity, MealTypeEntity> mealTypeJoin = recipeMealTypeXrefJoin.join("mealType", JoinType.LEFT);
            return criteriaBuilder.like(mealTypeJoin.get("name"), "%" + mealType + "%");
        };
    }

    public static Specification<RecipeEntity> isVegetarian(boolean isVegetarian) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isVegetarian"), isVegetarian);
    }

    public static Specification<RecipeEntity> isVegan(boolean isVegan) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isVegan"), isVegan);
    }

    public static Specification<RecipeEntity> isGlutenfree(boolean isGlutenfree) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isGlutenfree"), isGlutenfree);
    }

    public static Specification<RecipeEntity> isLactosefree(boolean isLactosefree) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isLactosefree"), isLactosefree);
    }
}
