package org.fairdo.benchmark.signposting;

import java.net.URI;
import java.util.Optional;

import org.fairdo.benchmark.api.FDOContentType;
import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.PID.URIPID;

public class SignpostingType implements FDOContentType {
	
	private static final URI LEGACY_NS = URI.create("http://schema.org/");
	private static final URI NS = URI.create("https://schema.org/");
	static final URI THING = NS.resolve("Thing");
	public static final SignpostingType UNKNOWN_TYPE = new SignpostingType(THING);
	static final URI ABOUT_PAGE = NS.resolve("AboutPage");
	public static final SignpostingType LANDING_TYPE = new SignpostingType(ABOUT_PAGE);
	private final URIPID pid;
	
	public SignpostingType(URI pid) {
		URI possiblyLegacy = LEGACY_NS.relativize(pid);
		if (! possiblyLegacy.isAbsolute()) {
			// Legacy type, replace http://schema with https://schema
			pid = NS.resolve(possiblyLegacy);
		}
		this.pid = new URIPID(pid);
	}

	public Optional<PID> pid() {		
		return Optional.of(pid);
	}
	
	@Override
	public String toString() {
		// Hide "https://schema.org/"
		return NS.relativize(pid.asURI()).toString();		
	}
	
	@Override
	public int hashCode() {
		return pid.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof FDOContentType)) {
			return false;
		}
		FDOContentType type = (FDOContentType) obj;
		return type.pid().isPresent() && pid.equals(type.pid().get());
	}
	
}
