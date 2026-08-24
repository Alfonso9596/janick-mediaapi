package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.RecipeMealTypeXrefEntity;
import com.janickmediadb.janickmediaapi.input.RecipeInput;
import com.janickmediadb.janickmediaapi.model.RecipeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = RecipeEntity.RECIPE_TABLE_NAME)
public class RecipeEntity extends AbstractEntity {

    static final String RECIPE_TABLE_NAME = "recipe";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    private String posterFilepath;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isGlutenfree;
    private boolean isLactosefree;
    private Instant createdAt;
    private Instant lastUpdated;

    @ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeMealTypeXrefEntity> recipeMealTypeXrefs = new HashSet<>();

    public void fromInput(RecipeInput recipeInput) {
        this.name = recipeInput.getName();
        this.description = recipeInput.getDescription();
        this.isVegetarian = recipeInput.isVegetarian();
        this.isVegan = recipeInput.isVegan();
        this.isGlutenfree = recipeInput.isGlutenfree();
        this.isLactosefree = recipeInput.isLactosefree();
    }

    public RecipeModel toModel(String timezone) {
        RecipeModel model = new RecipeModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setPosterFilepath(posterFilepath);
        model.setVegetarian(isVegetarian);
        model.setVegan(isVegan);
        model.setGlutenfree(isGlutenfree);
        model.setLactosefree(isLactosefree);
        model.setCreatedAt(createdAt.atZone(ZoneId.of(timezone)));
        model.setLastUpdated(lastUpdated.atZone(ZoneId.of(timezone)));
        model.setUser(user.toModel());
        return model;
    }
}
