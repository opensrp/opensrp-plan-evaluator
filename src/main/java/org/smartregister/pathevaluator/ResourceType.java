/**
 * 
 */
package org.smartregister.pathevaluator;

import org.smartregister.domain.Action.SubjectConcept;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public enum ResourceType {
	
	JURISDICTION("jurisdiction"),
	LOCATION("location"),
	FAMILY("family"),
	FAMILY_MEMBER("family_ember"),
	TASK("task"),
	QUESTIONAIRRE_RESPONSE("questionnaire_response");
	
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
	
	public static ResourceType from(SubjectConcept subjectConcept) {
		return from(subjectConcept.getText());
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
