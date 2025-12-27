package trpg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"trpg.web", "main"})
public class TrpgApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrpgApplication.class, args);
    }
}
