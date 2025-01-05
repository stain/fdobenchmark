package org.fairdo.benchmark.signposting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;
import org.junit.jupiter.api.Test;

public class LinkParserTest {
	@Test
	void parser() {
		Optional<Link> profile = LinkParser.parseLinks("<http://example.com/>; rel=profile").findAny();
		assertFalse(profile.isEmpty());
		Link link = profile.get();
		assertEquals("http://example.com/", link.getHref());
		assertEquals(IanaLinkRelations.PROFILE, link.getRel());
	}
}
