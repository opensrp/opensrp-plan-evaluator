/**
 * 
 */
package org.smartregister.datamask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.smartregister.datamask.DataMask.MaskType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author Samuel Githengi created on 10/28/20
 */
public class DataMaskSerializer extends StdSerializer<Object> implements ContextualSerializer {
	
	private static final long serialVersionUID = 1L;
	
	private MaskType maskType;
	
	public DataMaskSerializer() {
		super(Object.class);
	}
	
	public DataMaskSerializer(MaskType maskType) {
		super(Object.class);
		this.maskType = maskType;
	}
	
	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty property) {
		Optional<DataMask> anno = Optional.ofNullable(property).map(prop -> prop.getAnnotation(DataMask.class));
		return new DataMaskSerializer(anno.map(DataMask::type).orElse(null));
	}
	
	@Override
	public void serialize(Object obj, JsonGenerator gen, SerializerProvider prov) throws IOException {
		if (MaskType.STRING.equals(maskType)) {
			gen.writeString(StringUtils.repeat("x", obj.toString().length()));
			//TODO handle maps for attributes and identifiers
		} else {
			Calendar cal = Calendar.getInstance();
			if (obj instanceof Date) {
				cal.setTime((Date) obj);
			} else if (obj instanceof DateTime) {
				cal.setTime(((DateTime) obj).toDate());
			} else if (obj instanceof LocalDate) {
				cal.setTime(((LocalDate) obj).toDate());
			}
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, 0);
			//TODO write date with a date format
			gen.writeString(cal.getTime().toString());
		}
		
	}
	
}
