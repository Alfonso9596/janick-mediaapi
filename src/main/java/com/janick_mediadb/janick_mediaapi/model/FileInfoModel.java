package com.janick_mediadb.janick_mediaapi.model;

import com.janick_mediadb.janick_mediaapi.enums.DownloadFileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoModel extends AbstractModel {

    private String name;
    private String url;
    private long size;
    private DownloadFileType fileType;
    private List<FileInfoModel> children = new ArrayList<>();
}
