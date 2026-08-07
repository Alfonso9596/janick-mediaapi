package com.janick_mediadb.janick_mediaapi.service;

import java.nio.file.Path;

public interface MediaService {

    Path getMediaFilePath(int mediaId);
}
