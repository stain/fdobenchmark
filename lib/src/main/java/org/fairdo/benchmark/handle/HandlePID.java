package org.fairdo.benchmark.handle;

import java.net.URI;
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
		return handle;
	}
	
	public String handle() {
		return handle;
	}

	@Override
	public String toString() {
		return "Handle " + asString();
	}
	
}
