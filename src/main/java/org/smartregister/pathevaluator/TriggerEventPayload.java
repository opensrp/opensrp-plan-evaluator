package org.smartregister.pathevaluator;

import jdk.internal.jline.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.smartregister.domain.Jurisdiction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TriggerEventPayload {

	private TriggerEvent triggerEvent;

	@Nullable
	private List<Jurisdiction> jurisdictions;
}
