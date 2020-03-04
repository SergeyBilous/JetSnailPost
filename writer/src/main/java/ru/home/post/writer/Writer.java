package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.home.post.writer.repositories.PackageRepository;

@SpringBootApplication
@ComponentScan(basePackages = "ru.home.post.writer")
@EnableAutoConfiguration
public class Writer implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(Writer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CreatePackages parcelCreator = new CreatePackages(25);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(parcelCreator);
        Thread parcelCreatorThread = new Thread(parcelCreator);
        parcelCreatorThread.start();
    }
}
