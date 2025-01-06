package org.fairdo.benchmark.signposting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;
import org.junit.jupiter.api.Test;

public class LinkParserTest {
	
	URI A2A_01 = URI.create("https://w3id.org/a2a-fair-metrics/01-http-describedby-only/");
	
	@Test
	void parser() {
		Optional<Link> profile = LinkParser.parseLinks("<http://example.com/>; rel=profile").findAny();
		assertFalse(profile.isEmpty());
		Link link = profile.get();
		assertEquals("http://example.com/", link.getHref());
		assertEquals(IanaLinkRelations.PROFILE, link.getRel());
	}
	
	@Test
	void links() throws IOException { 
		HTTPSignposting signposting = new HTTPSignposting(A2A_01);
		System.out.println(signposting);
		//signposting.get
		
	}
}
