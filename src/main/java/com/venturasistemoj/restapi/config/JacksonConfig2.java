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
 * [EN] This class provides a configuration to customize the serialization and deserialization of Jackson
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
 * [PT] Esta classe fornece uma configuração para personalizar a serialização e desserialização de objetos LocalDate
 * no Jackson em um ambiente Spring.
 *
 * A classe é anotada com @Configuration, indicando que contém definições de bean do Spring. O bean definido é do tipo
 * Jackson2ObjectMapperBuilderCustomizer, que permite a customização do Jackson ObjectMapper usado pelo Spring.
 *
 * No método customizeJacksonObjectMapper(), o SimpleModule é responsável por registrar serializadores e
 * desserializadores personalizados. Aqui, um LocalDateSerializer e um LocalDateDeserializer para a classe LocalDate.
 *
 * A classe interna estática LocalDateSerializer é um serializador personalizado para JsonSerializer<LocalDate>.
 * Ela sobrescreve o método serialize() para especificar como um objeto LocalDate deve ser convertido em JSON. Aqui,
 * formata LocalDate como uma string "dd/MM/yyyy" e grava no JsonGenerator.
 *
 * A classe ingterna estática LocalDateDeserializer é um desserializador personalizado para JsonDeserializer<LocalDate>.
 * Ela sobrescreve o método desserialize() para especificar como uma string JSON deve ser convertida em um objeto
 * LocalDate. Aqui, espera que a string esteja no formato "dd/MM/yyyy" e a converte usando o DateTimeFormatter.
 *
 * Finalmente, o SimpleModule é incluído no Jackson2ObjectMapperBuilder chamando
 * jacksonObjectMapperBuilder.modules(module), registrando os serializadores e desserializadores no ObjectMapper
 * gerenciado pelo Spring.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Configuration
public class JacksonConfig2 {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Serializador responsável por converter um objeto LocalDate em uma representação  string no formato "dd/MM/yyyy"
	 * durante a serialização para JSON.
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
	 * Desserializador responsável por converter uma string no formato "dd/MM/yyyy" em um objeto LocalDate durante
	 * a desserialização do JSON.
	 */
	private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

		@Override
		public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			String dateString = jsonParser.getText();
			return LocalDate.parse(dateString, formatter);
		}
	}

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

}
