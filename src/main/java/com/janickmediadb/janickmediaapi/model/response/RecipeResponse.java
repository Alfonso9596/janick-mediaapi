package com.janickmediadb.janickmediaapi.model.response;

import com.janickmediadb.janickmediaapi.model.RecipeModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse extends AbstractResponse {
    private List<RecipeModel> content;
}
