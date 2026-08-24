package com.janickmediadb.janickmediaapi.utils;

public class NamingUtility {

    private NamingUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static String renameTitleForFilepath(String title, String year) {
        String cleanTitle = returnCleanFilepathValue(title);
        return cleanTitle + "_" + year;
    }

    public static String renameTitleForMusicFilepath(String title, String artist, String year) {
        String cleanTitle = returnCleanFilepathValue(title);
        String cleanArtist = returnCleanFilepathValue(artist);
        return cleanTitle + "_" + cleanArtist + "_" + year;
    }

    public static String returnCleanFilepathValue(String value) {
        return value.toLowerCase()
                .replace(":", "")
                .replace(" - ", "_")
                .replace("-", " ")
                .replace(" ", "_");
    }
}
