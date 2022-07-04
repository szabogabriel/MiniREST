package minirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class CompiledPathTest {
	
	private final String DEFAULT_REGEX = "([a-zA-Z0-9_\\\\.\\\\-]*)";
	
	@Test
	public void createSimplePath() {
		CompiledPath cp = new CompiledPath("/test");
		
		assertEquals(cp.getRegex(), "/test");
		assertEquals(cp.getGroupnames().length, 0);
	}
	
	@Test
	public void createNamedPathAtTheEnd() {
		CompiledPath cp = new CompiledPath("/test/{first}");
		
		assertEquals(cp.getRegex(), "/test/" + DEFAULT_REGEX);
		assertEquals(cp.getGroupnames().length, 1);
		assertEquals(cp.getGroupnames()[0], "first");
	}
	
	private void createNamedPathWithSpecialCharacter(String character) {
		CompiledPath cp = new CompiledPath("/test/{first}");
		
		Map<String, String> data = cp.produceValues("/test/asdf" + character + "ASDF" + character + "0123");
		
		assertEquals(data.size(), 1);
		assertEquals(data.get("first"), "asdf" + character + "ASDF" + character + "0123");
	}
	
	@Test
	public void createNamedPathWithDash() {
		createNamedPathWithSpecialCharacter("-");
	}
	
	@Test
	public void createNamedPathWithUnderscore() {
		createNamedPathWithSpecialCharacter("_");
	}
	
	@Test
	public void createNamedPathWithDot() {
		createNamedPathWithSpecialCharacter(".");
	}
	
	@Test
	public void createNamedPathInTheMiddle() {
		CompiledPath cp = new CompiledPath("/test/{first}/test");
		
		assertEquals(cp.getRegex(), "/test/" + DEFAULT_REGEX + "/test");
		assertEquals(cp.getGroupnames().length, 1);
		assertEquals(cp.getGroupnames()[0], "first");
	}
	
	@Test
	public void createSeveralNamedPath() {
		CompiledPath cp = new CompiledPath("/test/{first}/div/{second}/div/{third}/div");
		
		assertEquals(cp.getRegex(), "/test/" + DEFAULT_REGEX + "/div/" + DEFAULT_REGEX + "/div/" + DEFAULT_REGEX + "/div");
		assertEquals(cp.getGroupnames().length, 3);
		assertEquals(cp.getGroupnames()[0], "first");
		assertEquals(cp.getGroupnames()[1], "second");
		assertEquals(cp.getGroupnames()[2], "third");
	}
	
	@Test
	public void checkMatches() {
		CompiledPath cp = new CompiledPath("/test/{first}");
		
		assertTrue(cp.matches("/test/ohyeah"));
	}
	
	@Test
	public void checkValues() {
		CompiledPath cp = new CompiledPath("/test/{first}/{second}");
		
		Map<String, String> data = cp.produceValues("/test/a/b");
		
		assertNotNull(data);
		assertEquals(data.size(), 2);
		assertEquals(data.get("first"), "a");
		assertEquals(data.get("second"), "b");
	}
	
	@Test
	public void matchEmptyValue() {
		CompiledPath cp = new CompiledPath("/test/{first}/div/{second}");
		
		Map<String, String> data = cp.produceValues("/test//div/data");
		
		assertNotNull(data);
		assertEquals(data.size(), 2);
		assertEquals(data.get("first"), "");
		assertEquals(data.get("second"), "data");
	}
	
	@Test
	public void checkNotMatch() {
		CompiledPath cp = new CompiledPath("/test/{first}");
		
		Map<String, String> data = cp.produceValues("/test");
		
		assertEquals(data.size(), 0);
	}
	
	@Test
	public void checkEquality() {
		CompiledPath cp1 = new CompiledPath("/test1");
		CompiledPath cp2 = new CompiledPath("/test2");
		CompiledPath cp3 = new CompiledPath("/test1");
		
		assertNotEquals(cp1.hashCode(), cp2.hashCode());
		assertFalse(cp1.equals(cp2));
		
		assertTrue(cp1.equals(cp1));
		assertTrue(cp1.equals(cp3));
		assertFalse(cp1.equals(null));
		assertFalse(cp1.equals("nope"));
		assertFalse(cp1.equals(new CompiledPath(null)));
		assertFalse(new CompiledPath(null).equals(cp2));
		assertTrue(new CompiledPath(null).equals(new CompiledPath(null)));
		
		assertNotEquals(cp1.hashCode(), new CompiledPath(null).hashCode());
	}

}
