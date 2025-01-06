package org.fairdo.benchmark.signposting;

import java.util.Set;

import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.ContentType.IANAMediaType;

public class MediaTypeWithProfile extends IANAMediaType {

	public MediaTypeWithProfile(String mediaType, Set<String> profile) {
		super(mediaType);
		
	}
	@Override
	public Set<PID> profiles() {
		// TODO Auto-generated method stub
		return super.profiles();
	}

}
