/**
 * 
 */
package org.smartregister.pathevaluator;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public enum ResourceType {
	
	JURISDICTION("Jurisdiction"),
	LOCATION("Location"),
	FAMILY("Family"),
	FAMILY_MEMBER("Family_Member"),
	TASK("Task");
	
	private String value;
	
	private ResourceType(String value) {
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
	
	public static ResourceType from(String value) {
		for (ResourceType r : ResourceType.values()) {
			if (r.value.equals(value)) {
				return r;
			}
		}
		throw new ResourceTypeUnknownException(value);
	}
	
}
