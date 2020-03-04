package ru.home.post.writer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.home.post.writer")
@EnableAutoConfiguration
public class Mover implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Mover.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CreateMoves movesCreator = new CreateMoves(25);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(movesCreator);
        Thread movesCreatorThread=new Thread(movesCreator);
        movesCreatorThread.start();
    }
}
