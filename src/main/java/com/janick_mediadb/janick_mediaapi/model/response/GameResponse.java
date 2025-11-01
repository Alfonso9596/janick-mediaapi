package com.janick_mediadb.janick_mediaapi.model.response;

import com.janick_mediadb.janick_mediaapi.model.GameModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse extends AbstractResponse {
    private List<GameModel> content;
}
