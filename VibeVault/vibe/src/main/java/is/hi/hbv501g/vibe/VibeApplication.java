package is.hi.hbv501g.vibe;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.UserRepository;
import is.hi.hbv501g.vibe.Services.Implementation.EventServiceImplementation;

@SpringBootApplication
public class VibeApplication {
	private static final Logger log = LoggerFactory.getLogger(VibeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(VibeApplication.class, args);
	}

	// bara notað til að prófa tengingu milli gagnagrunns og forrits
	@Bean
	public CommandLineRunner demo(UserRepository repository, EventServiceImplementation eService) {
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
			Group testGroup = new Group("test");
			Date testDate = new Date(0);
			Event testEvent = new Event("test event", testDate, "this is a test event", "upcoming", testGroup, user2);
			eService.save(testEvent);
			eService.findAll().forEach(event -> {
				log.info(event.toString());
			});

		};
	}
}
