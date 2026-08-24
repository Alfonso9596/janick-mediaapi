package com.janickmediadb.janickmediaapi;

import com.janickmediadb.janickmediaapi.utils.NamingUtility;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestClass {

    private static final Path root = Paths.get("uploads");

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static void main(String[] args) {
        testNamingUtility("Avatar - Der Herr der Elemente", "2010");
    }

    private static void testNamingUtility(String title, String year) {
        System.out.println(NamingUtility.renameTitleForFilepath(title, year));
    }

    private static void testFileListing() throws IOException {
        Files.walk(root).filter(Files::isRegularFile).forEach(file -> System.out.println(file));
    }

    private static void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("admin"));
    }
}
