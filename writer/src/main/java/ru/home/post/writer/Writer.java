package ru.home.post.writer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.home.post.writer")
public class Writer implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Writer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CreatePackages parcelCreator = new CreatePackages(30);
        Thread parcelCreatorThread = new Thread(parcelCreator);
        parcelCreatorThread.start();
    }
}
