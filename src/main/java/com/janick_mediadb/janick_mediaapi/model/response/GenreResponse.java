package com.janick_mediadb.janick_mediaapi.model.response;

import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponse extends AbstractResponse {
    private List<MovieGenreModel> content;
}
