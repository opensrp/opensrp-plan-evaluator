/**
 * 
 */
package org.smartregister.pathevaluator;

/**
 * @author Samuel Githengi created on 06/11/20
 */
public enum TriggerType {
	
	PLAN_ACTIVATION("plan-activation"),
	PLAN_JURISDICTION_MODIFICATION("plan-jurisdiction-modification"),
	EVENT_SUBMISSION("event-submission");
	
	private String value;
	
	private TriggerType(String value) {
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	public String value() {
		return value;
	}
	
	public static TriggerType from(String value) {
		for (TriggerType r : TriggerType.values()) {
			if (r.value.equals(value)) {
				return r;
			}
		}
		throw new IllegalArgumentException("Trigger type not  valid");
	}
}
