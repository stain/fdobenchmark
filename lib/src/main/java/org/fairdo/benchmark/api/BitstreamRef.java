package org.fairdo.benchmark.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

public interface BitstreamRef {
	InputStream asInputStream() throws IOException;
	Optional<URI> asURI() throws IOException;
	ContentType contentType() throws IOException; 
	
}
