package likelion.lionboys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LionBoysApplication {
    public static void main(String[] args) {
        SpringApplication.run(LionBoysApplication.class, args);
    }
}
