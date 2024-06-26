package org.fairdo.benchmark.handle;

import java.net.URI;
import java.util.Locale;
import java.util.Objects;

import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.PID.AbstractPID;

public final class HandlePID extends AbstractPID {

	private URI NAMESPACE = URI.create("http://hdl.handle.net/");

	private PID RFC3651 = new HandlePID("10.17487/RFC3651");
	
	private String handle;
	
	public HandlePID(String handle) {
		this.handle = Objects.requireNonNull(handle);
	}
	
	public static HandlePID fromPID(PID pid) {
		if (pid instanceof HandlePID) { 
			return (HandlePID)pid;
		}
		URI uri = pid.asURI();
		if (uri.getScheme().equals("doi")) { // doi:10.12345/something  
			return new HandlePID(uri.getSchemeSpecificPart());
		}
		if (uri.getHost().equals("hdl.handle.net") || uri.getHost().equals("doi.org") || uri.getHost().equals("dx.doi.org")) {
			// Extract everything after http://hdl.handle.net/  etc. by finding the relative path from "/"
			URI relativePart = uri.resolve("/").relativize(uri);
			if (! relativePart.isAbsolute()) {
				throw new IllegalArgumentException("Could not extract Handle from URI: " + uri);
			}
			return new HandlePID(relativePart.toString());
		}
		throw new IllegalArgumentException("Can't identify PID as being Handle based: " + pid);		
	}
	
	@Override
	public PID getPIDType() {
		return RFC3651;
	}

	@Override
	public URI asURI() {
		// FIXME: This will not escape all characters correctly. However we also
		// don't want to %2f escape "/" in DOIs etc.
		return NAMESPACE.resolve(asString());
	}

	@Override
	public String asString() {
		return handle().toLowerCase(Locale.ENGLISH);
	}
	
	public String handle() {
		return handle;
	}

	@Override
	public String toString() {
		return "Handle " + asString();
	}
	
}
