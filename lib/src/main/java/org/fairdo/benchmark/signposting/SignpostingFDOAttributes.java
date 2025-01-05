package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDOContentType;
import org.fairdo.benchmark.api.FDOProfile;
import org.fairdo.benchmark.api.FDOReference;
import org.fairdo.benchmark.api.MetadataRef;
import org.fairdo.benchmark.api.PID.URIPID;

public class SignpostingFDOAttributes implements FDOAttributes<URIPID> {

	private final URIPID pid; // not needed?
	private Signposting signposting;

	public SignpostingFDOAttributes(URIPID pid) throws IOException {
		this.pid = pid;
		this.signposting = new HTTPSignposting(pid.asURI()); 
	}

	@Override
	public FDOContentType contentType() {
		Set<URI> uris = signposting.getTypes();
		return new SignpostingType(uris.stream().
				filter(u -> ! u.equals(SignpostingType.ABOUT_PAGE)).
				findAny().orElse(SignpostingType.ABOUT_PAGE));		
	}

	@Override
	public FDOReference<URIPID, FDOProfile> profile() {
		Set<URI> uris = signposting.getProfiles();
		return null;
	}

	@Override
	public Set<FDOReference<URIPID, BitstreamRef>> bitstreams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<FDOReference<URIPID, MetadataRef>> metadata() {
		// TODO Auto-generated method stub
		return null;
	}

}
