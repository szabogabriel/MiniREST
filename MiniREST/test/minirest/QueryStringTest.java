package minirest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class QueryStringTest {
	
	private QueryString qs;
	
	@Before
	public void init() {
		qs = new QueryString();
	}
	
	@Test
	public void testEmptyQueryString() {
		Map<String, String> tmp = qs.parse("");
		
		assertEquals(tmp.keySet().size(), 0);
	}
	
	@Test
	public void testNull() {
		Map<String, String> tmp = qs.parse(null);
		assertEquals(tmp.keySet().size(), 0);
	}
	
	@Test
	public void testKeyOnly() {
		Map<String, String> tmp = qs.parse("key");
		
		assertEquals(tmp.keySet().size(), 1);
	}
	
	@Test
	public void testKeyValue() {
		Map<String, String> tmp = qs.parse("key=value");
		
		assertEquals(tmp.keySet().size(), 1);
		
		String value = tmp.get("key");
		
		assertNotNull(value);
		assertTrue(value.equals("value"));
	}
	
	@Test
	public void addSeveralKeys() {
		Map<String, String> tmp = qs.parse("key1&key2&key3");
		
		assertEquals(tmp.keySet().size(), 3);
	}
	
	@Test
	public void addSeveralKeyValues() {
		Map<String, String> tmp = qs.parse("key1=value1&key2=value2&key3=value3");
		
		assertEquals(tmp.keySet().size(), 3);
		
		for (int i = 1; i <= 3; i++)
			assertTrue(tmp.get("key" + i).equals("value" + i));
	}
	
	@Test
	public void qsWithEqualsAtTheEnd() {
		Map<String, String> tmp = qs.parse("key1=value&key2=");
		
		assertEquals(tmp.keySet().size(), 2);
		assertTrue(tmp.containsKey("key2"));
	}
	
	@Test
	public void addMixedKeyValues() {
		Map<String, String> tmp = qs.parse("key1=value1&standalone&key2=value2");
		
		assertEquals(tmp.keySet().size(), 3);
		
		assertTrue(tmp.get("key1").equals("value1"));
		assertTrue(tmp.get("key2").equals("value2"));
		
		assertTrue(tmp.containsKey("standalone"));
		assertNull(tmp.get("standalone"));
	}

}
