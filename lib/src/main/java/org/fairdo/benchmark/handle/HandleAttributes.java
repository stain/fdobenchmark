package org.fairdo.benchmark.handle;

import java.util.Collections;
import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDOContentType;
import org.fairdo.benchmark.api.FDOProfile;
import org.fairdo.benchmark.api.FDOReference;
import org.fairdo.benchmark.api.MetadataRef;

public class HandleAttributes implements FDOAttributes<HandlePID> {

	private HandlePID pid;
	public HandleAttributes(HandlePID pid) {
		this.pid = pid;
	}

	@Override
	public FDOContentType contentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDOReference<HandlePID, FDOProfile> profile() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<FDOReference<HandlePID, BitstreamRef>> bitstreams() {
		// TODO: Check with resolver if there are multiple
		return Collections.singleton(new HandleStreamReference(pid));
	}

	@Override
	public Set<FDOReference<HandlePID, MetadataRef>> metadata() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}


}
