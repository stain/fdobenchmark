package org.fairdo.benchmark.api;

import java.util.Optional;

/**
 * 
 * FIXME: Unclear if "FDO-Content-Type" is separate from {@link ContentType}
 * linked from a {@link BitstreamRef}, or what are the properties of an FDO
 * Content Type.
 */
public interface FDOContentType {

	default Optional<PID> pid() { 
		return Optional.empty();
	}
}
