package config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.function.context.FunctionScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@FunctionScan
@Configuration
public class Config {

	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	@Primary
	public DataSource dataSource() {
		StringBuilder url = new StringBuilder()
				.append("jdbc:mysql://").append(System.getenv("RDS_ENDPOINT"))
				.append("/").append(System.getenv("RDS_DATABESNAME"))
				.append("?").append("characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
		return DataSourceBuilder
	        .create()
	        .username(System.getenv("RDS_USERNAME"))
	        .password(System.getenv("RDS_PASSWORD"))
	        .url(url.toString())
	        .build();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public AmazonSNS amazonSns() {
		return AmazonSNSClientBuilder
		.standard()
		.withRegion(System.getenv("REGION"))
		.build();
	}
	
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns) {
		return new NotificationMessagingTemplate(amazonSns);
	}
}
