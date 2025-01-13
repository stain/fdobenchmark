package org.fairdo.benchmark.signposting;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.DocumentParser;

import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;

public class HTMLSignposting extends AbstractSignposting {

	static class ParserCallbackExtension extends ParserCallback {
		private final AtomicBoolean finished;
		private final List<MutableAttributeSet> linkAttrs;

		private ParserCallbackExtension(AtomicBoolean finished, List<MutableAttributeSet> linkAttrs) {
			this.finished = finished;
			this.linkAttrs = linkAttrs;
		}

		@Override
		public void handleStartTag(Tag t, MutableAttributeSet attr, int pos) {
			if (t.equals(Tag.LINK)) {
				// For some reason these expire after parsing, so let's copy them
				linkAttrs.add(new SimpleAttributeSet(attr));					
			}
		}

		@Override
		public void handleSimpleTag(Tag t, MutableAttributeSet attr, int pos) {
			if (t.equals(Tag.LINK)) {
				linkAttrs.add(new SimpleAttributeSet(attr));
			}
		}

		@Override
		public void handleEndTag(Tag t, int pos) {
			if (t.equals(Tag.HEAD)) {
				finished.set(true);
				// No need to continue as all <link> should be in <head>, 
				// do an evil exception to stop parsing				
			}
		}
	}

	/**
	 * 
	 * @see https://chatgpt.com/share/67843c0f-c55c-8012-b523-59e0ffc9bd47
	 * @author ChatGPT 
	 */
	static class StreamReader extends Reader {
		private final Iterator<String> lineIterator;
		private String currentLine;
		private int currentIndex;
		private AtomicBoolean finished;
		private Stream<String> stream;

		public StreamReader(Stream<String> stream, AtomicBoolean finished) {
			this.stream = stream;
			this.finished = finished;
			this.lineIterator = stream.iterator();
			this.currentLine = null;
			this.currentIndex = 0;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			if (currentLine == null && !loadNextLine()) {
				return -1; // End of stream
			}

			int charsRead = 0;

			while (len > 0) {
				if (currentLine == null && !loadNextLine()) {
					break; // No more data
				}

				int charsToCopy = Math.min(len, currentLine.length() - currentIndex);
				currentLine.getChars(currentIndex, currentIndex + charsToCopy, cbuf, off);
				off += charsToCopy;
				len -= charsToCopy;
				charsRead += charsToCopy;
				currentIndex += charsToCopy;

				if (currentIndex >= currentLine.length()) {
					currentLine = null; // Move to the next line
					currentIndex = 0;
				}
			}

			return charsRead > 0 ? charsRead : -1; // Return -1 if no characters were read
		}

		@Override
		public void close() throws IOException {
			stream.close();
		}

		private boolean loadNextLine() {
			if (lineIterator.hasNext() && ! finished.get()) {
				currentLine = lineIterator.next() + "\n"; // Add newline to preserve line breaks
				currentIndex = 0;
				return true;
			} else {
				currentLine = null;
				return false;
			}
		}
	}

//	protected static final Object PROFILE = new HTML.Attribute("profile");

	public HTMLSignposting(URI uri) throws IOException {
		super(findLinks(uri));
	}

	private static Set<Link> findLinks(URI uri) throws IOException {
		HttpRequest req = HttpRequest.newBuilder().uri(uri).
		// Browser-like request for landing page (from Firefox)
				header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").
				GET().build();

		final AtomicBoolean mayFinishNow = new AtomicBoolean();
		try {
			HttpResponse<Stream<String>> resp = HttpClient.newBuilder()
					// Follow any PID redirects
					.followRedirects(Redirect.ALWAYS).build()
					.send(req, BodyHandlers.ofLines());
			final List<MutableAttributeSet> linkAttrs = new LinkedList<MutableAttributeSet>();
			try (StreamReader reader = new StreamReader(resp.body(), mayFinishNow)) { 
				DocumentParser parser = new DocumentParser(DTD.getDTD("html32"));
				parser.parse(reader, new ParserCallbackExtension(mayFinishNow, linkAttrs), false);
			}
			
			return linkAttrs.stream().flatMap(attr -> {
               String rels = (String) attr.getAttribute(HTML.Attribute.REL);               
               if (rels == null) { 
            	   return Stream.empty();
               }
               Stream<LinkRelation> linkRels = IanaLinkRelations.manyOf(rels.split(" +"));
               return linkRels.map(rel -> {
            	   return asLink(rel, attr);
               }); 
			}).collect(Collectors.toUnmodifiableSet());
		} catch (InterruptedException e) {
			// Unexpected interruption
			throw new IOException(e);
		}
	}
	
	public static Link asLink(LinkRelation linkRel, MutableAttributeSet attr) {
        String href = (String) attr.getAttribute(HTML.Attribute.HREF);
        String type = (String) attr.getAttribute(HTML.Attribute.TYPE); // May be null!
        String title = (String) attr.getAttribute(HTML.Attribute.TITLE);
 	   	return new Link(href, linkRel).withType(type).withTitle(title);
 	   	// .withProfile(profile)
	}


}
