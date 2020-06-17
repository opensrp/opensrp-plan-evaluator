/**
 * 
 */
package org.smartregister.pathevaluator;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class ResourceTypeUnknownException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	/**
	 * @param string
	 */
	public ResourceTypeUnknownException(String message) {
		this.message = message;
	}
	
	
	public String getMessage() {
		return message;
	}
	
}
