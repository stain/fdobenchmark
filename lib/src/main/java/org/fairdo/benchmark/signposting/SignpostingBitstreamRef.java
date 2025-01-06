package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.ContentType;
import org.fairdo.benchmark.api.PID;
import org.fairdo.benchmark.api.PID.URIPID;

public class SignpostingBitstreamRef implements BitstreamRef {

	private URIPID pid;
	private ContentType contentType;

	public SignpostingBitstreamRef(URIPID pid, ContentType contentType) {
		this.pid = Objects.requireNonNull(pid);
	}

	@Override
	public InputStream asInputStream() throws IOException {
		Builder builder = HttpRequest.newBuilder().uri(pid.asURI()).GET();
		String mediaType = contentType().mediaType();
		if (! mediaType.equals(ContentType.APPLICATION_OCTET_STREAM)) {
			// Basic content negotiation
			builder.header("Accept", mediaType);		
		}	
		for (PID p : contentType().profiles()) {
			// Experimental implementation of
			// https://datatracker.ietf.org/doc/html/draft-svensson-profiled-representations-00
			builder.header("Accept-Profile", p.asURI().toASCIIString());
		}		
		
		HttpRequest req = builder.build(); 
		try {
			HttpResponse<InputStream> resp = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).
					build().send(req,
					BodyHandlers.ofInputStream());
			return resp.body();	
		} catch (InterruptedException e) {
			throw new IOException(e);
		}		
	}

	@Override
	public Optional<URI> asURI() {
		return Optional.of(pid.asURI());
	}

	@Override
	public ContentType contentType() {
		return contentType;
	}

}
