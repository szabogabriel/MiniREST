package minirest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestHandlerOrchestrator<T> {
	
	private final String PREFIX;
	
	private final List<RequestHandler<T>> HANDLERS = new ArrayList<>(10);
	
	public RequestHandlerOrchestrator() {
		this("");
	}
	
	public RequestHandlerOrchestrator(String prefix) {
		PREFIX = prefix;
	}
	
	public void setHandler(String handlerUrl, T handler) {
		CompiledPath compiled = new CompiledPath(handlerUrl);
		HANDLERS.add(new RequestHandler<T>(compiled, handler));
		
	}
	
	public Optional<HandlerData<T>> getHandler(String path) {
		Optional<HandlerData<T>> ret = Optional.empty();
		
		if (isAcceptablePath(path)) 
			ret = findHandler(trimPath(path));
		
		return ret;
	}
	
	private Optional<HandlerData<T>> findHandler(String path) {
		Path parsedPath = new Path(path);
		
		for (RequestHandler<T> handler : HANDLERS)
			if (handler.COMPILED_PATH.matches(parsedPath.getPath()))
				return Optional.of(createHandler(parsedPath, handler));
		
		return Optional.empty();
	}
	
	private HandlerData<T> createHandler(Path qs, RequestHandler<T> handler) {
		Map<String, String> params = qs.getQueryStringParams();
		
		params.putAll(createParamsFromPath(qs, handler));
		
		HandlerData<T> ret = new HandlerData<T>(params, handler.HANDLER);
		return ret;
	}

	private Map<String, String> createParamsFromPath(Path qs, RequestHandler<T> handler) {
		return handler.COMPILED_PATH.produceValues(qs.getPath());
	}
	
	private String trimPath(String path) {
		String ret = path.substring(PREFIX.length());
		return ret;
	}
	
	private boolean isAcceptablePath(String path) {
		boolean ret = path.startsWith(PREFIX);
		return ret;
	}
	
	private class RequestHandler<L> {
		private final CompiledPath COMPILED_PATH;
		private final L HANDLER;
		
		public RequestHandler(CompiledPath path, L handler) {
			this.COMPILED_PATH = path;
			this.HANDLER = handler;
		}
	}
	
}
