package org.smartregister.domain;

import org.joda.time.DateTime;

public class Period {
	private DateTime start;

	private DateTime end;

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

}
