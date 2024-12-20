package org.fairdo.benchmark.handle;

import java.io.IOException;
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
import org.fairdo.benchmark.api.FDOReference;

public class HandleStreamReference implements FDOReference<HandlePID, BitstreamRef> {

	private HandlePID pid;

	public HandleStreamReference(HandlePID pid) {
		if (pid == null) {
			throw new NullPointerException();
		}
		this.pid = pid;
	}

	@Override
	public Optional<HandlePID> pid() {
		return Optional.of(pid);
	}


	private Optional<URI> findURI() throws IOException  {
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

	// FIXME: Handle system does not directly record expected content type 
	// so we have to try to resolve it
	private ContentType findContentType() throws IOException  {
		HttpRequest req = HttpRequest.newBuilder().uri(pid.asURI()).HEAD().build();
		try {
			HttpResponse<Void> resp = HttpClient.newBuilder()
					.followRedirects(Redirect.ALWAYS).build()
						.send(req, BodyHandlers.discarding());
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
	
	@Override
	public BitstreamRef resolve() throws IOException {
		return new HandleResolverStream(pid, findURI(), findContentType());
	}

}
