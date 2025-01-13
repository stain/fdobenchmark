package org.fairdo.benchmark.handle;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDOContentType;
import org.fairdo.benchmark.api.FDOReference;

import net.handle.api.HSAdapter;
import net.handle.api.HSAdapterFactory;
import net.handle.hdllib.HandleException;
import net.handle.hdllib.HandleValue;

public class HandleAttributes implements FDOAttributes<HandlePID> {

	static HSAdapter hsAdapter = HSAdapterFactory.newInstance();
	
	private HandlePID pid;
	private HandleValue[] resolved;

	public HandleAttributes(HandlePID pid) throws HandleException {
		this.pid = pid;
		this.resolved = resolveHandleAttributes(pid);
	}

	private static HandleValue[] resolveHandleAttributes(HandlePID pid) throws HandleException {
		HandleValue[] resolved = hsAdapter.resolveHandle(pid.handle(), null, null);
		System.out.println(Arrays.asList(resolved));
		return resolved;
	}

	@Override
	public FDOContentType contentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDOReference<HandlePID, BitstreamRef> profile() {
		for (HandleValue hv : resolved) {
			if (hv.getTypeAsString().equals("FDO_Profile_Ref")) {
				return asHandleStreamReference(hv);
			}
		}
		return null;
	}
	
	@Override
	public Set<FDOReference<HandlePID, BitstreamRef>> bitstreams() {
		return Stream.of(resolved).filter(hv -> hv.getTypeAsString().equals("FDO_Data_Refs"))
				.map(this::asHandleStreamReference)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<FDOReference<HandlePID, BitstreamRef>> metadata() {
		return Stream.of(resolved).filter(hv -> hv.getTypeAsString().equals("FDO_MD_Refs"))
			.map(this::asHandleStreamReference)
			.collect(Collectors.toSet());
	}

	private FDOReference<HandlePID, BitstreamRef> asHandleStreamReference(HandleValue hv) {
		HandlePID pid = new HandlePID(hv.getDataAsString());
		return new HandleStreamReference(pid); 
	}
	
	@Override
	public String toString() {
		return "HandleAttritubes <" + pid + "> type: <" + contentType() + ">\n" + Arrays.asList(resolved);
	}

}
