package com.janick_mediadb.janick_mediaapi.utils;

public class NamingUtility {

    private NamingUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static String renameTitleForFilepath(String title, String year) {
        String cleanTitle = title.toLowerCase().replace(":", "").replace(" - ", "_").replace("-", " ").replace(" ", "_");
        return cleanTitle + "_" + year;
    }

    public static String renameTitleForMusicFilepath(String title, String artist, String year) {
        String cleanTitle = title.toLowerCase().replace(":", "").replace(" - ", "_").replace("-", " ").replace(" ", "_");
        String cleanArtist = artist.toLowerCase().replace(":", "").replace(" - ", "_").replace("-", " ").replace(" ", "_");
        return cleanTitle + "_" + cleanArtist + "_" + year;
    }
}
