package org.fairdo.benchmark.signposting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;
import org.junit.jupiter.api.Test;

public class LinkParserTest {
	
	URI WF_1062_2 = URI.create("https://doi.org/10.48546/workflowhub.workflow.1063.2");
	
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
		HTTPSignposting signposting = new HTTPSignposting(WF_1062_2);
		System.out.println(signposting);
		assertEquals(WF_1062_2, signposting.getCiteAs().get());
		BitstreamRef data = signposting.getData().stream().findAny().get();
		assertEquals(URI.create("https://workflowhub.eu/workflows/1063/ro_crate?version=2"), 
				data.asURI().get());
		assertEquals("application/zip", data.contentType().mediaType());
		assertEquals("https://w3id.org/ro/crate", 
				data.contentType().profiles().stream().findAny().get().asString());
		
		Optional<BitstreamRef> metadata = signposting.getMetadata().stream().filter(m -> m.contentType().mediaType().equals("application/ld+json")).findAny();
		assertEquals("https://workflowhub.eu/workflows/1063?version=2", metadata.get().asURI().get().toString());
		
		Optional<BitstreamRef> metadata2 = signposting.getMetadata().stream().filter(m -> m.contentType().mediaType().equals("application/ld+json")).findAny();
		assertEquals("https://workflowhub.eu/workflows/1063?version=2", metadata2.get().asURI().get().toString());
	}
}
