package minirest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class PathTest {
	
	private Path compiledPath;
	
	private Map<String, String> getParams(String path) {
		compiledPath = new Path(path);
		return compiledPath.getQueryStringParams();
	}
	
	@Test
	public void testEmptyQueryString() {
		Map<String, String> tmp = getParams("");
		
		assertEquals(tmp.keySet().size(), 0);
		assertEquals(compiledPath.getPath(), "");
		assertEquals(compiledPath.getQs(), "");
	}
	
	@Test
	public void testNull() {
		Map<String, String> tmp = getParams(null);
		
		assertEquals(tmp.keySet().size(), 0);
		
		assertTrue(compiledPath.getQs().isEmpty());
		assertTrue(compiledPath.getPath().isEmpty());
	}
	
	@Test
	public void testKeyOnly() {
		Map<String, String> tmp = getParams("?key");
		
		assertEquals(tmp.keySet().size(), 1);
		assertTrue(tmp.containsKey("key"));
		assertEquals(compiledPath.getPath(), "");
	}
	
	@Test
	public void testKeyValue() {
		Map<String, String> tmp = getParams("?key=value");
		
		assertEquals(tmp.keySet().size(), 1);
		
		String value = tmp.get("key");
		
		assertNotNull(value);
		assertTrue(value.equals("value"));
		assertEquals(compiledPath.getPath(), "");
		assertEquals(compiledPath.getQs(), "key=value");
	}
	
	@Test
	public void addSeveralKeys() {
		Map<String, String> tmp = getParams("?key1&key2&key3");
		
		assertEquals(tmp.keySet().size(), 3);
	}
	
	@Test
	public void addSeveralKeyValues() {
		Map<String, String> tmp = getParams("?key1=value1&key2=value2&key3=value3");
		
		assertEquals(tmp.keySet().size(), 3);
		
		for (int i = 1; i <= 3; i++)
			assertTrue(tmp.get("key" + i).equals("value" + i));
	}
	
	@Test
	public void qsWithEqualsAtTheEnd() {
		Map<String, String> tmp = getParams("?key1=value&key2=");
		
		assertEquals(tmp.keySet().size(), 2);
		assertTrue(tmp.containsKey("key2"));
	}
	
	@Test
	public void addMixedKeyValues() {
		Map<String, String> tmp = getParams("?key1=value1&standalone&key2=value2");
		
		assertEquals(tmp.keySet().size(), 3);
		
		assertTrue(tmp.get("key1").equals("value1"));
		assertTrue(tmp.get("key2").equals("value2"));
		
		assertTrue(tmp.containsKey("standalone"));
		assertNull(tmp.get("standalone"));
	}
	
	@Test
	public void testQsFromPath() {
		Map<String, String> tmp = getParams("/mypath/values?key1=value1&key2");
		
		assertEquals(tmp.size(), 2);
		assertTrue(tmp.containsKey("key1"));
		assertTrue(tmp.containsKey("key2"));
		
		assertEquals(compiledPath.getPath(), "/mypath/values");
	}

	@Test
	public void testEmptyQsFromPath() {
		compiledPath = new Path("/mypath/values?");
		Map<String, String> tmp = compiledPath.getQueryStringParams();
		
		assertTrue(tmp.isEmpty());
		assertEquals(compiledPath.getPath(), "/mypath/values");
		assertEquals(compiledPath.getQs(), "");
		
		compiledPath = new Path("/mypath/values");
		
		assertTrue(tmp.isEmpty());
		assertEquals(compiledPath.getPath(), "/mypath/values");
		assertEquals(compiledPath.getQs(), "");
	}
	
}
