package org.fairdo.benchmark.api;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The content type of a bitstream, typically an IANA media type,
 * but other types are possible by having their own PIDs.
 * 
 * @see BitstreamRef
 */
public interface ContentType extends FDOContentType {
	
	public class IANAMediaType implements ContentType {

		private static final URI IANA = URI.create("https://www.iana.org/assignments/media-types/");
		private String mediaType;

		public IANAMediaType(String mediaType) {
			this.mediaType = mediaType;
		} 
		@Override
		public String mediaType() {
			return mediaType;
		}
		@Override
		public Optional<PID> pid() {
			// These are not ideal as they don't all resolve, but other PURLs
			// for media types unfortunately seem to break over time, see
			// https://github.com/nicholascar/mediatypes-service/issues/2
			return Optional.of(new PID.URIPID(IANA.resolve(mediaType)));
		}
	}
		
	static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	static final IANAMediaType UNKNOWN_TYPE = new IANAMediaType(APPLICATION_OCTET_STREAM);

	/**
	 * The type as a RFC6838 media type, excluding attributes like profile=
	 * 
	 * For example: <code>"application/xml"</code>
	 * 
	 * @see <https://www.rfc-editor.org/rfc/rfc6838.html>
	 * @see UNKNOWN_TYPE
	 * @return The RFC6838 media type, or "application/octet-stream" if undefined/unmatched. 
	 */
	default String mediaType() {
		return APPLICATION_OCTET_STREAM;
	}
	
	default Optional<PID> pid() { 
		return Optional.empty();
	}
	
	default Set<PID> profiles() {
		return Collections.emptySet();
	};
	
}
