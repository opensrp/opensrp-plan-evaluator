package org.smartregister.domain;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Period {
	private DateTime start;

	private DateTime end;

}
