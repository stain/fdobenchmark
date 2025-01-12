package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Set;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.DocumentParser;

import org.fairdo.benchmark.api.BitstreamRef;

public class HTMLSignposting implements Signposting {
	
//	protected static final Object PROFILE = new HTML.Attribute("profile");

	public HTMLSignposting(URI uri) throws IOException {
		findHeaders(uri);
	}
	
	private static HttpHeaders findHeaders(URI uri) throws IOException {
		HttpRequest req = HttpRequest.newBuilder().uri(uri).
		// Browser-like request for landing page (from Firefox)
				header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").GET().build();
		try {
			HttpResponse<String> resp = HttpClient.newBuilder()
					// Follow any PID redirects
					.followRedirects(Redirect.ALWAYS).build().send(req, BodyHandlers.ofString());
			String body = resp.body();
			DTD html = DTD.getDTD("html32");
			DocumentParser parser = new DocumentParser(html);
			parser.parse(new StringReader(body), new ParserCallback(){
				@Override
				public void handleStartTag(Tag t, MutableAttributeSet attr, int pos) {
					if (t.equals(Tag.LINK)) {
						Object rel = attr.getAttribute(HTML.Attribute.REL);
						Object href = attr.getAttribute(HTML.Attribute.HREF);
						Object type = attr.getAttribute(HTML.Attribute.TYPE);
						//Object profile = attr.getAttribute(PROFILE);
						System.out.println("" + rel + href + type);
					}
				}
				
				@Override
				public void handleSimpleTag(Tag t, MutableAttributeSet attr, int pos) {
					if (t.equals(Tag.LINK)) {
					}
				}
			}, false);
			return null;
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

	@Override
	public Set<BitstreamRef> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BitstreamRef> getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
