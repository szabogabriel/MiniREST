package minirest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		
		Optional<String> handler = restOrchestrator.getHandler("/test");
		
		assertTrue(handler.isPresent());
		assertEquals(handler.get(), "rest");
	}
	
	@Test
	public void testNotFoundHandler() {
		restOrchestrator.register("/test", "test");
		
		Optional<String> handler = restOrchestrator.getHandler("/nope");
		
		assertFalse(handler.isPresent());
	}
	
	@Test
	public void testNotAcceptablePath() {
		restOrchestrator = new RestOrchestrator<>("/prefix");
		
		restOrchestrator.register("/test", "test");
		
		assertFalse(restOrchestrator.getHandler("/test").isPresent());
		assertTrue(restOrchestrator.getHandler("/prefix/test").isPresent());
	}

}
