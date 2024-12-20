package org.fairdo.benchmark.api;

import java.io.IOException;
import java.util.Optional;

public interface FDOReference<PIDProfile extends PID, FDOObject> {
	Optional<PIDProfile> pid();
	FDOObject resolve() throws IOException;
}
