package com.venturasistemoj.restapi.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This class provides a configuration to customize the serialization and deserialization of Jackson
 * <code>LocalDate</code> objects in a Spring environment.
 *
 * The class is annotated with <code>@Configuration</code>, indicating that it contains Spring bean definitions. The
 * defined bean is of type <code>Jackson2ObjectMapperBuilderCustomizer</code>, which allows customization of the Jackson
 * <code>ObjectMapper</code> used by Spring.
 *
 * In the <code>customizeJacksonObjectMapper()</code> method, a <code>SimpleModule</code> is used to register custom
 * serializers and deserializers. It includes a <code>LocalDateSerializer</code> and a <code>LocalDateDeserializer</code>
 * specifically for the LocalDate class.
 *
 * The <code>LocalDateSerializer</code> is a custom serializer that extends <code>JsonSerializer<LocalDate></code>.
 * It overrides the <code>serialize()</code> method to specify how a <code>LocalDate</code> object should be converted
 * to JSON. In this case, it formats the <code>LocalDate</code> as a string in the format 'dd/MM/yyyy' and writes it to
 * the <code>JsonGenerator<code>.
 *
 * The <code>LocalDateDeserializer</code> is a custom deserializer that extends <code>JsonDeserializer<LocalDate></code>.
 * It overrides the <code>deserialize()<code> method to specify how a JSON string should be converted to a
 * <code>LocalDate</code> object. It expects the string to be in the format 'dd/MM/yyyy' and parses it using the
 * <code>DateTimeFormatter</code>.
 *
 * Finally, the <code>SimpleModule/<code> is added to the <code>Jackson2ObjectMapperBuilder</code> by calling
 * <code>jacksonObjectMapperBuilder.modules(module)</code>, ensuring that the custom serializers and deserializers are
 * registered with the Spring-managed <code>ObjectMapper</code>.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Configuration
public class JacksonConfig {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Nested class responsible for converting a <code>LocalDate</code> object into a string representation in the
	 * format "dd/MM/yyyy" during serialization to JSON.
	 */
	private static class LocalDateSerializer extends JsonSerializer<LocalDate> {

		@Override
		public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			String formattedDate = localDate.format(formatter);
			jsonGenerator.writeString(formattedDate);
		}
	}

	/**
	 * Nested class responsible for converting a string in the format "dd/MM/yyyy" into a <code>LocalDate</code> object
	 * during JSON deserialization.
	 */
	private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

		@Override
		public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			String dateString = jsonParser.getText();
			return LocalDate.parse(dateString, formatter);
		}
	}

	/**
	 * Defines custom serializers and deserializers for <code>LocalDate</code> using static nested classes
	 * (<code>LocalDateSerializer</code> and <code>LocalDateDeserializer</code>), which implement the
	 * <code>JsonSerializer</code> and <code>JsonDeserializer</code> interfaces respectively.
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeJacksonObjectMapper() {

		/**
		 * Configuring <code>ObjectMapper</code> is done by creating a <code>SimpleModule</code>, where custom
		 * serializers and deserializers are registered for the <code>LocalDate</code> class. The <code>SimpleModule</code>
		 * is added to the <code>Jackson2ObjectMapperBuilder</code> using the <code>modules()</code> method.
		 */
		return jacksonObjectMapperBuilder -> {

			SimpleModule module = new SimpleModule();
			module.addSerializer(LocalDate.class, new LocalDateSerializer());
			module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

			jacksonObjectMapperBuilder.modules(module); // Spring
		};
	}


}
