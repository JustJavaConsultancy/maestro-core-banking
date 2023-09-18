package ng.com.systemspecs.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Api Gateway.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	public final Mail mail = new Mail();	
	
	public Mail getMail() {
		return mail;
	}



	public static class Mail {
		String from = "";
		String host = "";
		int port = 0;		
		boolean auth;
		boolean starttls;
		String username = "";
		String password = "";
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public boolean getStarttls() {
			return starttls;
		}
		public void setStarttls(boolean starttls) {
			this.starttls = starttls;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean getAuth() {
			// TODO Auto-generated method stub
			return auth;
		}
		
		public void setAuth(boolean auth) {
			// TODO Auto-generated method stub
			this.auth = auth;
		}
		
		
	}
}
