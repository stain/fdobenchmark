package org.fairdo.benchmark.handle;

import java.util.Collections;
import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttribute;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDORecord;

import net.handle.hdllib.HandleException;

public class HandleRecord implements FDORecord<HandlePID, byte[], byte[]> {

	private HandlePID pid;
	private HandleAttributes mandatory;

	public HandleRecord(HandlePID pid) throws HandleException {
		this.pid = pid; 
		this.mandatory = new HandleAttributes(pid);
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
		return mandatory;
	}

	@Override
	public Set<FDOAttribute<byte[], byte[]>> optionalAttributes() {
		return Collections.emptySet();
	}

}
