/**
 * 
 */
package org.fairdo.benchmark.api;

import java.util.Set;

/**
 * 
 */
public interface FDORecord<PIDType extends PID, KeyType, ValueType> {
	PIDType pid();
	PIDType pidProfile();
	Set<PIDType> types();
	Set<MetadataRef<PIDType>> metadata();
	Set<BitstreamRef> bitstreams();
	Set<FDOAttribute<KeyType,ValueType>> getAttributes();
}
