package com.janickmediadb.janickmediaapi.model;

import com.janickmediadb.janickmediaapi.enums.DownloadFileType;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
