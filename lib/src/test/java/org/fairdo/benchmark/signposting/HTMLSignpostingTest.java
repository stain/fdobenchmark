package org.fairdo.benchmark.signposting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.Test;

public class HTMLSignpostingTest {
	@Test
	void parser() throws IOException {
		HTMLSignposting s = new HTMLSignposting(URI.create("http://dev.s11.no/2022/a2a-fair-metrics/02-html-full/"));
		assertEquals(URI.create("https://w3id.org/a2a-fair-metrics/02-html-full/"), s.getCiteAs().get());
		
	}
}
