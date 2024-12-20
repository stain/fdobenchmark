package org.fairdo.benchmark.handle;

import java.util.Collections;
import java.util.Set;

import org.fairdo.benchmark.api.FDOAttribute;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDORecord;

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
	public FDOAttributes<HandlePID> mandatoryAttributes() {
		return new HandleAttributes(pid);
	}

	@Override
	public Set<FDOAttribute<byte[], byte[]>> optionalAttributes() {
		return Collections.emptySet();
	}

}
