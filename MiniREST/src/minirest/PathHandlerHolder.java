package minirest;

import java.util.Map;

public class PathHandlerHolder<L> {
	
	private final Map<String, String> PARAMS;
	private final L HANDLER;
	
	public PathHandlerHolder(Map<String, String> params, L handler) {
		this.PARAMS = params;
		this.HANDLER = handler;
	}
	
	public Map<String, String> getParams() {
		return PARAMS;
	}
	
	public L getHandler() {
		return HANDLER;
	}

}
