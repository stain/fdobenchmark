package org.fairdo.benchmark.handle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HandleResolverTest {
	private static final String KIT_EXAMPLE = "21.T11967/1a7708f65582256a4538";

	@Test
	void testName() throws Exception {
		HandlePID ex = new HandlePID(KIT_EXAMPLE);
		HandleRecord exRecord = new HandleRecord(ex);
		System.out.println(exRecord.mandatoryAttributes().metadata());
	}
}
