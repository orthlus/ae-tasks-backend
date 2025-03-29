package art.aelaort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public UserDetailsService userDetailsService(
			@Value("${security.user}") String user,
			@Value("${security.password}") String password
	) {
		UserDetails userDetails = User.builder()
				.username(user)
				.password(password)
				.build();
		return new InMemoryUserDetailsManager(userDetails);
	}
}
