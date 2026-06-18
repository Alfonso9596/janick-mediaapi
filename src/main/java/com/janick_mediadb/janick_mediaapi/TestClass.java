package com.janick_mediadb.janick_mediaapi;

import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class TestClass {

    private static final Path root = Paths.get("uploads");

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        testNamingUtility("Avatar - Der Herr der Elemente");
    }

    private static void testNamingUtility(String title) {
        System.out.println(NamingUtility.renameTitleForFilepath(title));
    }

    private static void testFileListing() throws IOException {
        Files.walk(root).filter(Files::isRegularFile).forEach(file -> System.out.println(file));
    }

    private static void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("admin"));
    }
}
