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

public class HTTPSignposting implements Signposting {

	private List<String> headers;

	public HTTPSignposting(URI uri) throws IOException {
		this.headers = findHeaders(uri).allValues("Link");
		// TODO: Parse Link headers
	}
	
	@Override
	public String toString() {
		return headers.toString();
	}

	private HttpHeaders findHeaders(URI uri) throws IOException {
		HttpRequest req  = HttpRequest.newBuilder().uri(uri).
				// 	Browser-like request for landing page (from Firefox)			
				header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").
				HEAD().build();	
		try {
			HttpResponse<Void> resp = HttpClient.newBuilder()
					// Follow any PID redirects
					.followRedirects(Redirect.ALWAYS).build().send(req,
							BodyHandlers.discarding());
			return resp.headers();	
		} catch (InterruptedException e) {
			throw new IOException(e);
		}					
	}

	@Override
	public Set<URI> getTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<URI> getProfiles() {
		// TODO Auto-generated method stub
		return null;
	}

}
