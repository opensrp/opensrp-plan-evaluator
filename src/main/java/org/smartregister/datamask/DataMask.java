/**
 * 
 */
package org.smartregister.datamask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Samuel Githengi created on 10/28/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD )
@JacksonAnnotationsInside
@JsonSerialize(using = DataMaskSerializer.class)
public @interface DataMask {
	
	enum MaskType {
		DATE,
		STRING
	};
	
	public MaskType type() default MaskType.STRING;
}
