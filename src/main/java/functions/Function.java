package functions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import functions.Function.Input;
import functions.Function.Output;


public class Function implements java.util.function.Function<Input, Output> {

	private final JdbcTemplate jdbcTemplate;
	private final NotificationMessagingTemplate notificationMessagingTemplate;

	public Function(JdbcTemplate jdbcTemplate, NotificationMessagingTemplate notificationMessagingTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.notificationMessagingTemplate = notificationMessagingTemplate;
	}

	@Override
	public Output apply(final Input input) {

		System.out.println(input);
		
		final Output output = new Output();
        output.setList(this.findByQuery());
        this.notificationMessagingTemplate.sendNotification(System.getenv("SNS_TOPIC"), output.getList(), System.getenv("SNS_SUBJECT"));
        
		return output;
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findByQuery() {
		final Resource resource = new ClassPathResource("/query.sql", getClass());
        try (
        	final LineNumberReader reader = new LineNumberReader(new InputStreamReader(resource.getInputStream()));
        ) {
        	final String query = ScriptUtils.readScript(reader, "--", ";");
        	return jdbcTemplate.queryForList(query);
        } catch (IOException e) {
        	throw new RuntimeException(e);
		}
	}
	
	public static final class Input {
	}
	
	public static final class Output {
		private List<Map<String, Object>> list;

		public List<Map<String, Object>> getList() {
			return list;
		}

		public void setList(final List<Map<String, Object>> list) {
			this.list = list;
		}
		
	}
	
}
