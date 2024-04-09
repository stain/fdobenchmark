package org.fairdo.benchmark.api;

import java.net.URI;
import java.net.URISyntaxException;

public interface PID {
	
	// TODO: Replace with https://w3id.org/ PIDs or Handles
	static PID URI_PID_TYPE = new URIPID(URI.create("http://hdl.handle.net/10.17487/RFC3986"));
	static PID UNKNOWN_PID_TYPE = new URIPID(URI.create("urn:uuid:58ff4890-977e-40a0-9b5f-2d77be82b864"));


	public final class URIPID extends AbstractPID { 
		public URIPID(URI uri) {
			this.uri = uri;
		}
		
		public URIPID(String string) throws URISyntaxException {
			this(new URI(string));
		}

		private URI uri;

		@Override
		public URI asURI() {
			return uri;
		}

		@Override
		public PID getPIDType() {
			return URI_PID_TYPE;
		}

		@Override
		public String asString() {
			return asURI().toASCIIString();
		}
		
		@Override
		public String toString() {
			return "<" + asString() + ">";
		}
	}
	
	public abstract class AbstractPID implements PID {
		

		@Override
		public PID getPIDType() {
			return UNKNOWN_PID_TYPE;
		}
		
	
		@Override
		public int hashCode() {
			return asURI().hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (! (obj instanceof PID)) {
				return false;
			}
			PID pid = (PID)obj;
			if (getPIDType() == pid.getPIDType() || getPIDType().equals(pid.getPIDType())) {
				// Note: object identity used above to avoid infinite recursion on URI_PID_TYPE.equals(URI_PID_TYPE)
				return asString().equals(pid.asString());
			}
			// Different PID types, but they may have equal URIs, e.g. https://hdl.handle.net/10.1234/abc
			return asURI().equals(pid.asURI());
		}
		
		@Override
		public String toString() {
			return "PID <" + asString() + "> (type unknown)";
		}
		
	}
	
	PID getPIDType();	
	
	URI asURI();

	String asString();
	
}
