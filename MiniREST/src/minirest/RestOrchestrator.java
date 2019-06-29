package minirest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestOrchestrator<T> {
	
	private final String PREFIX;
	
	private final List<PathHandler<T>> HANDLERS = new ArrayList<>(10);
	
	public RestOrchestrator() {
		this("");
	}
	
	public RestOrchestrator(String prefix) {
		PREFIX = prefix;
	}
	
	public void register(String handlerUrl, T handler) {
		CompiledPath compiled = new CompiledPath(handlerUrl);
		HANDLERS.add(new PathHandler<T>(compiled, handler));
		
	}
	
	public Optional<T> getHandler(String path) {
		Optional<T> ret = Optional.empty();
		
		if (isAcceptablePath(path)) 
			ret = findHandler(trimPath(path));
		
		return ret;
	}
	
	private Optional<T> findHandler(String path) {
		for (PathHandler<T> handler : HANDLERS)
			if (handler.PATH.matches(path))
				return Optional.of(handler.HANDLER);
		
		return Optional.empty();
	}
	
	private String trimPath(String path) {
		String ret = path.substring(PREFIX.length());
		return ret;
	}
	
	private boolean isAcceptablePath(String path) {
		boolean ret = path.startsWith(PREFIX);
		return ret;
	}
	
	private class PathHandler<L> {
		private final CompiledPath PATH;
		private final L HANDLER;
		
		public PathHandler(CompiledPath path, L handler) {
			this.PATH = path;
			this.HANDLER = handler;
		}
	}
	
}
