package com.janick_mediadb.janick_mediaapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileInfoModel extends AbstractModel {

    private String name;
    private String url;
}
