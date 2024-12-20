/**
 * 
 */
package org.fairdo.benchmark.api;

import java.util.Set;

public interface FDORecord<PIDType extends PID, KeyType, ValueType> {
	PIDType pid();
	PIDType pidProfile(); // Is a PID Profile different from an FDO Profile?
	//Set<PIDType> types();  No longer required? See 
	FDOAttributes<PIDType> mandatoryAttributes();
	Set<FDOAttribute<KeyType,ValueType>> optionalAttributes();
}
