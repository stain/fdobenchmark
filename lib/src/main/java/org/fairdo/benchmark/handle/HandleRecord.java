package org.fairdo.benchmark.handle;

import java.util.Collections;
import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttribute;
import org.fairdo.benchmark.api.FDORecord;
import org.fairdo.benchmark.api.MetadataRef;

public class HandleRecord implements FDORecord<HandlePID, byte[], byte[]> {

	private HandlePID pid;

	public HandleRecord(HandlePID pid) {
		this.pid = pid; 
	}
	
	@Override
	public HandlePID pid() {
		return pid;
	}

	@Override
	public HandlePID pidProfile() {
		// TODO: Register PID for Handle PID Profile?
		return null;
	}

	@Override
	public Set<HandlePID> types() {
		// TODO: Is this where we return the "profile" from index 1?
	}

	@Override
	public Set<MetadataRef<HandlePID>> metadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BitstreamRef> bitstreams() {
		// TODO: Check with resolver if there are multiple
		return Collections.singleton(new HandleResolverStream(pid));
	}

	@Override
	public Set<FDOAttribute<byte[], byte[]>> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
