package com.janickmediadb.janickmediaapi.utils;

import com.janickmediadb.janickmediaapi.controller.FileController;
import com.janickmediadb.janickmediaapi.enums.DownloadFileType;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class FileUtility {

    private FileUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static List<FileInfoModel> getDirList(File node) {
        List<FileInfoModel> fileInfoModels = new ArrayList<>();
        for (File file : node.listFiles()) {
            fileInfoModels.add(getNode(file));
        }
        return fileInfoModels;
    }

    private static FileInfoModel getNode(File node) {
        FileInfoModel fileInfoModel = new FileInfoModel();
        fileInfoModel.setName(node.getName());
        if (node.isDirectory()) {
            fileInfoModel.setFileType(DownloadFileType.FOLDER);

            List<FileInfoModel> childrenInfoModel = getDirList(node);
            fileInfoModel.setChildren(childrenInfoModel);
        } else {
            String relativePath = node.toPath().toString().replace("uploads\\", "");
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", relativePath)
                    .toUriString();

            fileInfoModel.setUrl(url.replace("\\", "/"));
            fileInfoModel.setSize(node.length());

            String fileExtension = getFileExtension(node.getName());
            switch (fileExtension) {
                case "zip", "rar", "7z", "tar":
                    fileInfoModel.setFileType(DownloadFileType.ZIP);
                    break;
                case "txt":
                    fileInfoModel.setFileType(DownloadFileType.TEXT);
                    break;
                // TODO: Add extensions for specific game files if needed in future
                default:
                    fileInfoModel.setFileType(DownloadFileType.VIDEO);
            }
        }
        return fileInfoModel;
    }

    private static String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return null;
    }
}
