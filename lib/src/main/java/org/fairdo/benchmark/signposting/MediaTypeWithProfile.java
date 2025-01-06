package org.fairdo.benchmark.signposting;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import org.fairdo.benchmark.api.ContentType.IANAMediaType;
import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.PID.URIPID;

public class MediaTypeWithProfile extends IANAMediaType {

	private Set<PID> profiles;
	public MediaTypeWithProfile(String mediaType, Set<String> profiles) {
		super(mediaType);
		this.profiles = profiles.stream()
				.map(URI::create)
				.map(URIPID::new)
				.collect(Collectors.toSet());
	}
	
	@Override
	public Set<PID> profiles() {
		return profiles;
	}

}
