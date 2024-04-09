package org.fairdo.benchmark.handle;

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

public class HandleResolverStream implements BitstreamRef {

	private HandlePID pid;

	public HandleResolverStream(HandlePID pid) {
		this.pid = pid;
	}

	@Override
	public InputStream asInputStream() throws IOException {
		HttpRequest req = HttpRequest.newBuilder()
				// TODO: Use https resolver instead of http
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
			HttpResponse<Void> resp = HttpClient.newHttpClient().send(req, BodyHandlers.discarding());
			if (resp.statusCode() < 300 || resp.statusCode() > 399) {
				return Optional.empty();
			}
			Optional<String> loc = resp.headers().firstValue("Location");
			return loc.map(URI::create);
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
