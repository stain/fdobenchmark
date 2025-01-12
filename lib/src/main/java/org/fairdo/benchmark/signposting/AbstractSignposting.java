package org.fairdo.benchmark.signposting;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.ContentType;
import org.fairdo.benchmark.api.ContentType.IANAMediaType;
import org.fairdo.benchmark.api.PID.URIPID;
import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;

public class AbstractSignposting implements Signposting {

	private Set<Link> links;

	public AbstractSignposting(Set<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return links.toString();
	}

	@Override
	public Set<URI> getTypes() {
		return links.stream()
				.filter(e -> e.getRel().equals(IanaLinkRelations.TYPE))
				.map(l -> URI.create(l.getHref()))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<URI> getProfiles() {
		return links.stream()
				.filter(e -> e.getRel().equals(IanaLinkRelations.PROFILE))
				.map(l -> URI.create(l.getHref()))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<BitstreamRef> getMetadata() {
		return links.stream()
		.filter(e -> e.getRel().equals(IanaLinkRelations.DESCRIBED_BY))
		.map(this::asBitstreamRef).collect(Collectors.toSet());
	}

	private BitstreamRef asBitstreamRef(Link l) {
		return new SignpostingBitstreamRef(new URIPID(URI.create(l.getHref())), 
				asContentType(l));
	}

	private ContentType asContentType(Link l) {
		String type = l.getType().orElse(IANAMediaType.APPLICATION_OCTET_STREAM);		
		Set<String> profiles = l.getProfile().stream()
				.flatMap(s-> Arrays.asList(s.split(" +")).stream())
				.collect(Collectors.toSet());
		return new MediaTypeWithProfile(type, profiles);
	}

	@Override
	public Set<BitstreamRef> getData() {
		return links.stream()
		.filter(e -> e.getRel().equals(IanaLinkRelations.ITEM))
		.map(this::asBitstreamRef).collect(Collectors.toSet());
	}

	public Optional<URI> getCiteAs() {
		return links.stream()
				.filter(e -> e.getRel().equals(IanaLinkRelations.CITE_AS))
				.map(Link::getHref)
				.map(URI::create)
				//.map(URIPID::new)
				.findAny();
	}
}