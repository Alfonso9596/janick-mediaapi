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
                case "zip", "rar", "7z", "tar", "gz" -> fileInfoModel.setFileType(DownloadFileType.ZIP);
                case "txt" -> fileInfoModel.setFileType(DownloadFileType.TEXT);
                case "pdf" -> fileInfoModel.setFileType(DownloadFileType.PDF);
                case "png", "jpg", "jpeg", "webp", "tiff", "bmp", "svg", "gif" ->
                    fileInfoModel.setFileType(DownloadFileType.IMAGE);
                case "mp4", "mov", "mkv", "avi", "webm", "wmv", "flv" ->
                    fileInfoModel.setFileType(DownloadFileType.VIDEO);
                case "wav", "flac", "mp3", "wma", "aiff", "alac", "ape" ->
                    fileInfoModel.setFileType(DownloadFileType.AUDIO);
                case null, default -> fileInfoModel.setFileType(DownloadFileType.UNKNOWN);
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
