/**
 * 
 */
package org.smartregister.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Samuel Githengi created on 10/28/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DataMask {
	
	enum MaskType {
		DATE,
		STRING,
		MAP,
		ADDRESS
	};
	
	public MaskType type() default MaskType.STRING;
}
