package is.hi.hbv501g.vibe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class VibeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeApplication.class, args);
	
	}
}
