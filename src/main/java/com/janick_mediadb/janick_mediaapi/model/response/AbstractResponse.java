package com.janick_mediadb.janick_mediaapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse {
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
