package com.janickmediadb.janickmediaapi.model.response;

import com.janickmediadb.janickmediaapi.model.GenreModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponse extends AbstractResponse {
    private List<GenreModel> content;
}
