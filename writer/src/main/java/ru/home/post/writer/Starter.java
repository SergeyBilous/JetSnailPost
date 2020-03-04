package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"ru.home.post.writer.config"})
public class Starter implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Nothing to do");
        }
        switch (args[0].toUpperCase()) {
            case "MOVES": {
                CreateMoves createMoves = (CreateMoves) applicationContext.getBean("createMoves");
                Thread t = new Thread(createMoves);
                t.start();
            }
            break;
            case "PACKAGES": {
                CreatePackages createPackages = (CreatePackages) applicationContext.getBean("createPackages");
                Thread t = new Thread(createPackages);
                t.start();
            }
            break;
        }

    }
}
