package minirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestOrchestratorTest {
	
	private RequestHandlerOrchestrator<String> restOrchestrator;
	
	@BeforeEach
	void prepare() {
		restOrchestrator = new RequestHandlerOrchestrator<>();
	}
	
	@Test
	void testFoundHandler() {
		restOrchestrator.setHandler("/test", "rest");
		
		Optional<HandlerData<String>> handler = restOrchestrator.getHandler("/test");
		
		assertTrue(handler.isPresent());
		assertEquals(handler.get().getHandler(), "rest");
	}
	
	@Test
	void testNotFoundHandler() {
		restOrchestrator.setHandler("/test", "test");
		
		Optional<HandlerData<String>> handler = restOrchestrator.getHandler("/nope");
		
		assertFalse(handler.isPresent());
	}
	
	@Test
	void testNotAcceptablePath() {
		restOrchestrator = new RequestHandlerOrchestrator<>("/prefix");
		
		restOrchestrator.setHandler("/test", "test");
		
		assertFalse(restOrchestrator.getHandler("/test").isPresent());
		assertTrue(restOrchestrator.getHandler("/prefix/test").isPresent());
	}
	
	@Test
	void testParamsParsed() {
		restOrchestrator.setHandler("/test/{first}/{second}", "testHandler");
		
		Optional<HandlerData<String>> handler = restOrchestrator.getHandler("/test/paramA/paramB");
		
		assertTrue(handler.isPresent());
		
		Map<String, String> params = handler.get().getParams();
		assertEquals(params.size(), 2);
		assertTrue(params.containsKey("first"));
		assertTrue(params.containsKey("second"));
		assertEquals(params.get("first"), "paramA");
		assertEquals(params.get("second"), "paramB");
	}

}
