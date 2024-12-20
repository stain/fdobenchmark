package org.fairdo.benchmark.api;

import java.util.Set;

/**
 * Mandatory-FDO-Attributes
 * 
 * @param <PIDProfile> PID profile of any references
 */
public interface FDOAttributes<PIDProfile extends PID> {
	FDOContentType contentType();   // Is this different from content type of bitstreams
	FDOReference<PIDProfile, FDOProfile> profile();  // Only one profile??
	Set<FDOReference<PIDProfile,BitstreamRef>> bitstreams();
	Set<FDOReference<PIDProfile,MetadataRef>> metadata();
}
