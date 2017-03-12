package com.ssr.base.util.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 格式化JSON返回时的BigDecimal值
 * @author pl
 *
 */
public class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal>{

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		DecimalFormat df = new DecimalFormat("#,##,##0.00");
		gen.writeString(df.format(value));
	}

}
