# MiniREST

This application aims to be a simple mapping between the URL and the handlers registered in the
central `RequestHandlerOrchestrator` class. The usage of this library isn't necessarily bound only
to REST. HTTP methods are not part of this handler class, thus the handler itself should be able to
handle every expected HTTP method type.

## Usage
The `RequestHandlerOrchestrator<T>` class can be created with or without a preceding path. If the
path is set, it will only accept URLs starting with it (e.g. `new RequestHandlerOrchestrator("/test")`
will accept `/test/app` as a path, but wont accept `/app`.

After creating the `RequestHandlerOrchestrator<T>` instance, the handlers for a given path can be
entered via the `public void setHandler(String handlerUrl, T handler)` method. The `handlerUrl`
follows the JAX-RS notation of URL, where the names of the parameters can be entered by using `{}`;
for example `/path/{firstParam}/followingpath/{secondParam}`. This URL is compiled into a regular
expression. Only the following groupMembers are recognized: [a-zA-Z0-9]. Custom regular expressions
are currently not supported. When entering a path for parsing, empty parameter names are possible
by adding two slashes. E.g. using the previous path: `/path//followingpath/secondValue`.

The handler cen be received via `RequestHandlerOrchestrator.getHandler(String url)` method, which
returns an `Optional<HandlerData<T>>`. The `HandlerData` offers two methods to acquire the handler
itself, and the parsed parameters in form of a `Map<String, String>`.

Query strings are also parsed and put into the final map of parameters. Should duplicate parameters
occure in the query string and in the URL, the URL overrides the ones in the query string.
