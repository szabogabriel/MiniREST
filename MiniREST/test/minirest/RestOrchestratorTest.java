package minirest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class RestOrchestratorTest {
	
	private RestOrchestrator<String> restOrchestrator;
	
	@Before
	public void prepare() {
		restOrchestrator = new RestOrchestrator<>();
	}
	
	@Test
	public void testFoundHandler() {
		restOrchestrator.register("/test", "rest");
		
		Optional<PathHandlerHolder<String>> handler = restOrchestrator.getHandler("/test");
		
		assertTrue(handler.isPresent());
		assertEquals(handler.get().getHandler(), "rest");
	}
	
	@Test
	public void testNotFoundHandler() {
		restOrchestrator.register("/test", "test");
		
		Optional<PathHandlerHolder<String>> handler = restOrchestrator.getHandler("/nope");
		
		assertFalse(handler.isPresent());
	}
	
	@Test
	public void testNotAcceptablePath() {
		restOrchestrator = new RestOrchestrator<>("/prefix");
		
		restOrchestrator.register("/test", "test");
		
		assertFalse(restOrchestrator.getHandler("/test").isPresent());
		assertTrue(restOrchestrator.getHandler("/prefix/test").isPresent());
	}
	
	@Test
	public void testParamsParsed() {
		restOrchestrator.register("/test/{first}/{second}", "testHandler");
		
		Optional<PathHandlerHolder<String>> handler = restOrchestrator.getHandler("/test/paramA/paramB");
		
		assertTrue(handler.isPresent());
		
		Map<String, String> params = handler.get().getParams();
		assertEquals(params.size(), 2);
		assertTrue(params.containsKey("first"));
		assertTrue(params.containsKey("second"));
		assertEquals(params.get("first"), "paramA");
		assertEquals(params.get("second"), "paramB");
	}

}
