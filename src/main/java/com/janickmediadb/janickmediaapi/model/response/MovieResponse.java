package com.janickmediadb.janickmediaapi.model.response;

import com.janickmediadb.janickmediaapi.model.MovieModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse extends AbstractResponse {
    private List<MovieModel> content;
}
