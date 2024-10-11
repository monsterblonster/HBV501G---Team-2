package is.hi.hbv501g.vibe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.UserRepository;

@SpringBootApplication
public class VibeApplication {
	private static final Logger log = LoggerFactory.getLogger(VibeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(VibeApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			User user1 = new User("username1", "password1");
			User user2 = new User("Hackerman69", "BigBoi17");
			repository.save(user1);
			repository.save(user2);

			log.info("Users found with findAll():");
			repository.findAll().forEach(user -> {
				log.info(user.getUserName());
			});
			log.info(repository.findByUserName("username1").toString());
			log.info("");
		};
	}
}
