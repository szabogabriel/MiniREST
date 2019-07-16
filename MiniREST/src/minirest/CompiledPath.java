package minirest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompiledPath {
	
	private final Pattern PATTERN;
	
	private final String[] GROUPNAMES;
	
	private final String ORIGINAL;
	
	public CompiledPath(String path) {
		ORIGINAL = path;
		GROUPNAMES = new String[numberOfGroups(path)];
		PATTERN = Pattern.compile(createRegex(path));
	}
	
	private int numberOfGroups(String data) {
		int ret = 0;
		
		if (data != null)
			for (char it : data.toCharArray())
				if (it == '{')
					ret++;

		return ret;
	}
	
	private String createRegex(String path) {
		String ret = "";
		
		if (path != null) {
			char [] data = path.toCharArray();
			
			int groupid = 0;
			
			for (int i = 0; i < data.length; i++) {
				if (data[i] == '{') {
					String name = "";
					while (data[++i] != '}')
						name += data[i];
					
					GROUPNAMES[groupid++] = name;
					ret += "([a-zA-Z0-9_\\\\.\\\\-]*)";
				} else
					ret += data[i];
			}
		}
			
		return ret;
	}
	
	public String getRegex() {
		return PATTERN.toString();
	}
	
	public String[] getGroupnames() {
		return GROUPNAMES;
	}
	
	public boolean matches(String path) {
		return PATTERN.matcher(path).matches();
	}
	
	public Map<String, String> produceValues(String path) {
		Map<String, String> ret = new HashMap<>();
		
		Matcher matcher = PATTERN.matcher(path);
		
		if (matcher.matches())
			for (int i = 0; i < GROUPNAMES.length; i++)
				ret.put(GROUPNAMES[i], matcher.group(i + 1));
		
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ORIGINAL == null) ? 0 : ORIGINAL.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompiledPath other = (CompiledPath) obj;
		if (ORIGINAL == null) {
			if (other.ORIGINAL != null)
				return false;
		} else if (!ORIGINAL.equals(other.ORIGINAL))
			return false;
		return true;
	}


}
