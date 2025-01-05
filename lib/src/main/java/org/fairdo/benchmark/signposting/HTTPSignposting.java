package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;

public class HTTPSignposting implements Signposting {

	private Set<Link> links;

	public HTTPSignposting(URI uri) throws IOException {
		List<String> headers = findHeaders(uri).allValues("Link");
		this.links = findLinks(headers);
	}

	private static Set<Link> findLinks(List<String> headers) {
		return headers.stream().flatMap(e -> LinkParser.parseLinks(e)).collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return links.toString();
	}

	private static HttpHeaders findHeaders(URI uri) throws IOException {
		HttpRequest req = HttpRequest.newBuilder().uri(uri).
		// Browser-like request for landing page (from Firefox)
				header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").HEAD().build();
		try {
			HttpResponse<Void> resp = HttpClient.newBuilder()
					// Follow any PID redirects
					.followRedirects(Redirect.ALWAYS).build().send(req, BodyHandlers.discarding());
			return resp.headers();
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
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

}
