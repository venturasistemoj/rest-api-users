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
 * [EN] This class provides a configuration to customize the serialization and deserialization of Jackson LocalDate
 * objects in a Spring environment.
 *
 * The class is annotated with @Configuration, indicating that it contains Spring bean definitions. The defined bean
 * is of type Jackson2ObjectMapperBuilderCustomizer, which allows customization of the Jackson ObjectMapper used
 * by Spring.
 *
 * In the customizeJacksonObjectMapper() method, a SimpleModule is used to register custom serializers and deserializers.
 * It includes a LocalDateSerializer and a LocalDateDeserializer specifically for the LocalDate class.
 *
 * The LocalDateSerializer is a custom serializer that extends JsonSerializer<LocalDate>. It overrides the serialize()
 * method to specify how a LocalDate object should be converted to JSON. In this case, it formats the LocalDate as a
 * string in the format 'dd/MM/yyyy' and writes it to the JsonGenerator.
 *
 * The LocalDateDeserializer is a custom deserializer that extends JsonDeserializer<LocalDate>. It overrides the
 * deserialize() method to specify how a JSON string should be converted to a LocalDate object. It expects the string to
 * be in the format 'dd/MM/yyyy' and parses it using the DateTimeFormatter.
 *
 * Finally, the SimpleModule is added to the Jackson2ObjectMapperBuilder by calling
 * jacksonObjectMapperBuilder.modules(module), ensuring that the custom serializers and deserializers are registered
 * with the Spring-managed ObjectMapper.
 *
 * [PT] Esta classe fornece uma configuração para personalizar a serialização e desserialização de objetos LocalDate
 * no Jackson em um ambiente Spring.
 *
 * A classe é anotada com @Configuration, indicando que contém definições de bean do Spring. O bean definido é do tipo
 * Jackson2ObjectMapperBuilderCustomizer, que permite a customização do Jackson ObjectMapper usado pelo Spring.
 *
 * No método customizeJacksonObjectMapper(), o SimpleModule é responsável por registrar serializadores e
 * desserializadores personalizados. Aqui, um LocalDateSerializer e um LocalDateDeserializer para a classe LocalDate.
 *
 * A classe ingterna estática LocalDateSerializer é um serializador personalizado para JsonSerializer<LocalDate>.
 * Ela sobrescreve o método serialize() para especificar como um objeto LocalDate deve ser convertido em JSON. Aqui,
 * formata LocalDate como uma string "dd/MM/yyyy" e grava no JsonGenerator.
 *
 * A classe ingterna estática LocalDateDeserializer é um desserializador personalizado para JsonDeserializer<LocalDate>.
 *  Ela sobrescreve o método desserialize() para especificar como uma string JSON deve ser convertida em um objeto
 *  LocalDate. Aqui, espera que a string esteja no formato "dd/MM/yyyy" e a converte usando o DateTimeFormatter.
 *
 *  Finalmente, o SimpleModule é incluído no Jackson2ObjectMapperBuilder chamando
 *  jacksonObjectMapperBuilder.modules(module), registrando os serializadores e desserializadores no ObjectMapper
 *  gerenciado pelo Spring.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Configuration
public class JacksonConfig2 {

	/* Define serializadores e desserializadores personalizados para LocalDate utilizando classes internas estáticas
	 * (LocalDateSerializer e LocalDateDeserializer), que implementam as interfaces JsonSerializer e JsonDeserializer
	 * respectivamente.
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeJacksonObjectMapper() {

		/* A configuração do ObjectMapper é feita por meio da criação de um SimpleModule, onde os serializadores e
		 * desserializadores personalizados são registrados para a classe LocalDate. O SimpleModule é adicionado ao
		 * Jackson2ObjectMapperBuilder utilizando o método modules().
		 */
		return jacksonObjectMapperBuilder -> {

			SimpleModule module = new SimpleModule();
			module.addSerializer(LocalDate.class, new LocalDateSerializer());
			module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

			jacksonObjectMapperBuilder.modules(module); // Spring
		};
	}

	// serializador
	private static class LocalDateSerializer extends JsonSerializer<LocalDate> {

		private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		@Override
		public void serialize( LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			String formattedDate = localDate.format(formatter);
			jsonGenerator.writeString(formattedDate);
		}
	}

	// deserializador
	private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

		private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		@Override
		public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
			String dateString = jsonParser.getText();
			return LocalDate.parse(dateString, formatter);
		}
	}
}
