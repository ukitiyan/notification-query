package functions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;



//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@AutoConfigureMockMvc
public class FunctionTests {

	@Autowired
	MockMvc mvc;
	@Autowired
	Function function;

	//@Test
	public void testApply() throws Exception {
		mvc.perform(post("/function")).andExpect(status().isOk());
	}

}
