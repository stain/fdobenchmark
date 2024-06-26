package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import org.fairdo.benchmark.api.BitstreamRef;
import org.fairdo.benchmark.api.ContentType;
import org.fairdo.benchmark.api.ContentType.IANAMediaType;
import org.fairdo.benchmark.api.PID.URIPID;

public class HttpBitstreamRef implements BitstreamRef {

	private URIPID pid;

	public HttpBitstreamRef(URIPID pid) {
		if (! (pid.asURI().getScheme().equals("http") || pid.asURI().getScheme().equals("https"))) {
			throw new IllegalArgumentException("Unsupported URI scheme, expected http:// or https:// in PID URI " + pid.asURI());
		}
		this.pid = pid;
	}

	@Override
	public InputStream asInputStream() throws IOException {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(pid.asURI()).GET().build();
		try {
			HttpResponse<InputStream> resp = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).build().send(req,
					BodyHandlers.ofInputStream());
			return resp.body();
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Optional<URI> asURI() throws IOException {
		HttpRequest req = HttpRequest.newBuilder().uri(pid.asURI()).HEAD().build();
		try {
			HttpResponse<InputStream> resp = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).build().send(req,
					BodyHandlers.ofInputStream());
			if (resp.statusCode() == 200) { 
				return Optional.of(resp.uri());
			} { 
				// PID did not resolve/redirect correctly
				return Optional.empty();
			}
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	@Override
	public ContentType contentType() throws IOException {
		HttpRequest req = HttpRequest.newBuilder().uri(pid.asURI()).HEAD().build();
		try {
			HttpResponse<Void> resp = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).build().send(req,
					BodyHandlers.discarding());
			if (resp.statusCode() != 200) {
				return ContentType.UNKNOWN_TYPE;
			}
			// FIXME: Not checking for redirects may risk "text/html" for the hdl.handle.net
			// landing page in the case of no URL to redirect to.
			Optional<String> loc = resp.headers().firstValue("Content-Type");
			// FIXME: This should strip away any parameters like ;charset
			return loc.map(IANAMediaType::new).orElse(ContentType.UNKNOWN_TYPE);
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

}
