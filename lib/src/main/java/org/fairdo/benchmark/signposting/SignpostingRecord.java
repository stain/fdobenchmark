package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.fairdo.benchmark.api.FDOAttribute;
import org.fairdo.benchmark.api.FDOAttributes;
import org.fairdo.benchmark.api.FDORecord;
import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.PID.URIPID;

public class SignpostingRecord implements FDORecord<PID.URIPID, String, String>{

	// TODO: Make a better PID for our PID Profile
	private static final URIPID FAIR_PROFILE = new URIPID(URI.create("https://signposting.org/FAIR/"));
	private URIPID pid;

	@Override
	public URIPID pid() {
		return pid;
	}

	@Override
	public URIPID pidProfile() {
		return FAIR_PROFILE;
	}

	@Override
	public FDOAttributes<URIPID> mandatoryAttributes() {
		try {
			return new SignpostingFDOAttributes(pid);
		} catch (IOException e) {
			e.printStackTrace();			
			return null;
		}
	}

	@Override
	public Set<FDOAttribute<String, String>> optionalAttributes() {
		return Collections.emptySet();
	}

}
