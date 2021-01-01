package com.example.demo.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.io.ByteStreams;
import org.springframework.core.io.ClassPathResource;

/**
 * json-schema验证
 * @author raining_heavily
 * @date 2020年7月18日 17点10分
 **/
public class JsonSchemaUtil {

	private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

	/**
	 *
	 * @param mainSchema 验证规则
	 * @param instance 需要验证的内容
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static void validate(String mainSchema, String instance) throws IOException, ProcessingException {
		JsonNode mainNode = JsonLoader.fromString(mainSchema);
        JsonNode instanceNode = JsonLoader.fromString(instance);
        JsonSchema schema = factory.getJsonSchema(mainNode);
		ProcessingReport pr = schema.validate(instanceNode);
		Iterator<ProcessingMessage> iterator = pr.iterator();
		while (iterator.hasNext()){
			ProcessingMessage pm = iterator.next();
			pm.getMessage();
			System.out.print(pm);
			break;
		}
//		System.out.print(pr);
	}
	
	public static void main(String[] args) throws IOException, ProcessingException {
		ClassPathResource classPathResource = new ClassPathResource("json-schema/test.json");
		String mainSchema = new String(ByteStreams.toByteArray(classPathResource.getInputStream()));
		String instance = "{\"id\":\"12345\",\"name\":\"张三\" }";
		validate(mainSchema,instance);

	}
}
