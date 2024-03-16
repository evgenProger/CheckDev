import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeneratorSrv {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GeneratorSrv.class);
        application.run();
    }
}
