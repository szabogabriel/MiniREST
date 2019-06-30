package minirest;

import java.util.HashMap;
import java.util.Map;

public class Path {
	
	private final Map<String, String> PARAMS;
	private final String URL;
	private final String QS;
	
	public Path(String path) {
		if (path == null) {
			URL = "";
			QS = "";
			PARAMS = new HashMap<>();
		} else {
			URL = stripUrlFromQueryString(path);
			QS = stripQueryStringFromPath(path);
			PARAMS = parseQueryString(QS);
		}
	}
	
	public String getPath() {
		return URL;
	}
	
	public String getQs() {
		return QS;
	}
	
	public Map<String, String> getQueryStringParams() {
		return createCopyOfParams();
	}
	
	private Map<String, String> parseQueryString(String qs) {
		Map<String, String> ret = new HashMap<>();
		
		if (isQueryString(qs))
			for (String entry : splitQsIntoEntries(qs))
				ret.put(getKey(entry), getValue(entry));
		
		return ret;
	}
	
	private String stripUrlFromQueryString(String url) {
		String ret = url;
		
		if (containsQueryString(url))
			ret = url.substring(0, url.indexOf("?"));
			
		return ret;
	}
	
	private String [] splitQsIntoEntries(String qs) {
		String [] ret = qs.split("&");
		return ret;
	}
	
	private String getKey(String qs) {
		String ret = qs;
		if (isQsMadeOfKeyAndValue(qs))
			ret = qs.substring(0, qs.indexOf("="));
		return ret;
	}
	
	private String getValue(String qs) {
		String ret = null;
		if (isQsMadeOfKeyAndValue(qs) && isValueReallyPresent(qs))
			ret = qs.substring(qs.indexOf("=") + 1);
		return ret;
	}

	private Map<String, String> createCopyOfParams() {
		Map<String, String> ret = new HashMap<String, String>();
		ret.putAll(PARAMS);
		return ret;
	}
	
	private String stripQueryStringFromPath(String path) {
		String ret = "";
		
		if (containsQueryString(path))
				ret = path.substring(path.indexOf("?") + 1);
		
		return ret;
	}
	
	private boolean isValueReallyPresent(String qs) {
		return qs.indexOf("=") < qs.length() - 1;
	}

	private boolean isQsMadeOfKeyAndValue(String qs) {
		return qs.contains("=");
	}
	
	private boolean containsQueryString(String path) {
		boolean ret = path.contains("?");
		return ret;
	}
	
	private boolean isQueryString(String qs) {
		boolean ret = qs.trim().length() > 0;
		return ret;
	}

}
