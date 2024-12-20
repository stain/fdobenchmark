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

public class HandleResolverStream implements BitstreamRef {

	private HandlePID pid;
	private ContentType contentType;
	private Optional<URI> uri;

	public HandleResolverStream(HandlePID pid, Optional<URI> uri, ContentType contentType) {
		this.pid = pid;
		this.uri = uri;
		this.contentType = contentType;		
	}

	@Override
	public InputStream asInputStream() throws IOException {
		HttpRequest req = HttpRequest.newBuilder()
				// TODO: Use https resolver instead of http
				.uri(pid.asURI()).GET().build();
		try {
			HttpResponse<InputStream> resp = HttpClient.newBuilder()
					.followRedirects(Redirect.ALWAYS).build()
						.send(req,BodyHandlers.ofInputStream());
			return resp.body();
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Optional<URI> asURI() {
		return uri;
	}

	@Override
	public ContentType contentType() {
		return contentType;
	}


}
