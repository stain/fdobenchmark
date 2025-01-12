package org.fairdo.benchmark.signposting;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import org.fairdo.benchmark.api.BitstreamRef;

public interface Signposting {

	Optional<URI> getCiteAs();
	
	Set<URI> getTypes();

	Set<URI> getProfiles();

	Set<BitstreamRef> getMetadata();
	
	Set<BitstreamRef> getData();
	
}
