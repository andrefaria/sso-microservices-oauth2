package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.cloud.security.oauth2.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@EnableOAuth2Sso
public class SsoApplication {

    @Autowired
    private OAuth2ClientContext oauth2Context;

	@RequestMapping("/")
	public String home() {
		return "<html><body><a href='dashboard/'>dashboard</a><br/><a href='login'>login</a></body></html>";
	}

	@RequestMapping("/dashboard")
	public String dashboard(HttpServletRequest request) {
        return "<html><body>APP 1 says: welcome "+request.getRemoteUser()+" </body></html>";
	}

	public static void main(String[] args) {
		SpringApplication.run(SsoApplication.class, args);
	}

	@Component
	public static class LoginConfigurer extends OAuth2SsoConfigurerAdapter {
		@Override
		public void match(RequestMatchers matchers) {
			matchers.antMatchers("/dashboard/**");
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated();
		}
	}
}
