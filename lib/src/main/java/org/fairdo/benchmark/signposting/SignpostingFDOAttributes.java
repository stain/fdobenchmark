package org.fairdo.benchmark.signposting;

import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDOContentType;
import org.fairdo.benchmark.api.FDOProfile;
import org.fairdo.benchmark.api.FDOReference;
import org.fairdo.benchmark.api.MetadataRef;
import org.fairdo.benchmark.api.PID.URIPID;

public class SignpostingFDOAttributes implements FDOAttributes<URIPID> {

	@Override
	public FDOContentType contentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDOReference<URIPID, FDOProfile> profile() {
		// TODO Auto-generated method stub
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
