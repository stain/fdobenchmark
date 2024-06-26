package org.fairdo.benchmark.api;

import java.util.Optional;

public interface MetadataRef<PIDProfile extends PID> extends BitstreamRef {

	Optional<PIDProfile> pid();
}
