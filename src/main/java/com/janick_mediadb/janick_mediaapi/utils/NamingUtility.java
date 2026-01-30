package com.janick_mediadb.janick_mediaapi.utils;

public class NamingUtility {

    private NamingUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static String renameTitleForFilepath(String title) {
        return title.toLowerCase().replace(":", "").replace(" ", "_");
    }
}
