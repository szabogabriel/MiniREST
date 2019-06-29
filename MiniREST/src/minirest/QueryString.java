package minirest;

import java.util.HashMap;
import java.util.Map;

public class QueryString {
	
	public Map<String, String> parse(String qs) {
		Map<String, String> ret = new HashMap<>();
		
		if (isQueryString(qs))
			for (String entry : splitQsIntoEntries(qs))
				ret.put(getKey(entry), getValue(entry));
		
		return ret;
	}
	
	private String [] splitQsIntoEntries(String qs) {
		String [] ret = qs.split("&");
		return ret;
	}
	
	private String getKey(String qs) {
		String ret = qs;
		if (qs.contains("="))
			ret = qs.substring(0, qs.indexOf("="));
		return ret;
	}
	
	private String getValue(String qs) {
		String ret = null;
		if (qs.contains("=") && qs.indexOf("=") < qs.length() - 1)
			ret = qs.substring(qs.indexOf("=") + 1);
		return ret;
	}
	
	private boolean isQueryString(String qs) {
		boolean ret = (qs != null) && qs.trim().length() > 0;
		return ret;
	}

}
