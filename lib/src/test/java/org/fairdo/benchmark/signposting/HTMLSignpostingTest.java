package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.Test;

public class HTMLSignpostingTest {
	@Test
	void parser() throws IOException {
		HTMLSignposting s = new HTMLSignposting(URI.create("https://zbmed-semtec.github.io/projects/2022_maSMP/"));
	}
}
