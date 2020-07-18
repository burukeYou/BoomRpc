package burukeyou;

import burukeyou.discover.annoation.EnableServerRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableServerRegister
@SpringBootApplication
public class ServerA_Application {
    public static void main(String[] args) {
        SpringApplication.run(ServerA_Application.class,args);
    }
}
