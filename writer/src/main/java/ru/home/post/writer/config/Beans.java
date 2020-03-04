package ru.home.post.writer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.home.post.writer.CreateMoves;
import ru.home.post.writer.CreatePackages;
import ru.home.post.writer.Initializer;

import java.io.IOException;

@Lazy
@Configuration
public class Beans {
    @Bean("createPackages")
    public CreatePackages createPackages() {
        CreatePackages creator = new CreatePackages(25);
        return creator;
    }

    @Bean("createMoves")
    public CreateMoves createMoves() {
        CreateMoves creator = new CreateMoves(25);

        return creator;
    }

    @Bean("initializer")
    public Initializer createInitializer() {
        try {
            return new Initializer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
