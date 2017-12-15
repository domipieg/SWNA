package prz.swna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
//@EnableJpaRepositories("prz.swna.repositories")
public class SwnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwnaApplication.class, args);
	}
}
