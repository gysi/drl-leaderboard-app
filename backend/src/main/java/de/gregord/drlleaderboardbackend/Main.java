package de.gregord.drlleaderboardbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints({
        MyCustomRuntimeHints.class
})
public class Main {
    public static void main(String[] args) {
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(Main.class, args);
    }
}
