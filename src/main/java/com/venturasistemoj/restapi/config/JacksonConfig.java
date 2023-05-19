package com.venturasistemoj.restapi.config;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/*
 * [EN] Spring configuration class (@Configuration) to customize the ObjectMapper configuration.
 * @AutoConfigureBefore({JacksonAutoConfiguration.class}) indicates that the configuration defined in the @Bean should
 * be applied before the automatic configuration provided by the JacksonAutoConfiguration class - responsible for
 * configuring the Standard Jackson ObjectMapper in Spring context. This annotation overrides the default ObjectMapper
 * setting provided by Spring and provides a custom one.
 *
 * The class configures custom serializers and deserializers for LocalDate, LocalTime, and LocalDateTime.
 * They are registered in the ObjectMapper to handle converting the values of these types.
 * The Jackson2ObjectMapperBuilderCustomizer customizes the configuration of the ObjectMapper used by Spring to
 * serialize and automatically deserialize LocalDate, LocalTime and LocalDateTime types into the specified format,
 * without the need for @JsonFormat annotation in each entity field you need in the application.
 *
 * [PT] Classe de configuração (@Configuration) do Spring para personalizar a configuração do ObjectMapper.
 * @AutoConfigureBefore({JacksonAutoConfiguration.class}) indica que a configuração definida no @Bean deve ser aplicada
 * antes da configuração automática fornecida pela classe JacksonAutoConfiguration - responsável por configurar o
 * ObjectMapper padrão do Jackson no contexto do Spring. Essa anotação substitui a configuração padrão do ObjectMapper
 * fornecida pelo Spring e fornece uma personalizada.
 *
 * A classe configura serializadores e desserializadores personalizados para os tipos LocalDate, LocalTime e
 * LocalDateTime. Eles são registrados no ObjectMapper para lidar com a conversão dos valores desses tipos.
 * O Jackson2ObjectMapperBuilderCustomizer personaliza a configuração do ObjectMapper usado pelo Spring para serializar
 * e desserializar automaticamente os tipos LocalDate, LocalTime e LocalDateTime no formato especificado,
 * sem a necessidade da anotação @JsonFormat em cada campo de entidade que necessite na aplicação.
 */

@Configuration
@AutoConfigureBefore({JacksonAutoConfiguration.class})
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {

		/*
		 * [EN] jacksonObjectMapperBuilder configures serializers and deserializers for types LocalDate,
		 * LocalTime and LocalDateTime.
		 * [PT] O jacksonObjectMapperBuilder configura os serializadores e desserializadores para os tipos
		 * LocalDate, LocalTime e LocalDateTime.
		 */
		return jacksonObjectMapperBuilder -> {

			// representam os formatos de data, hora e data e hora, respectivamente.
			final String dateFormat = "dd/MM/yyyy";
			final String timeFormat = "hh:mm:ss a";
			final String dateTimeFormat = "dd/MM/yyyy hh:mm:ss a";

			/*
			 * [EN] LocalDateSerializer, LocalTimeSerializer and LocalDateTimeSerializer configure a custom serializer
			 * Jackson's StdSerializer and DateTimeFormatter.ofPattern formats types.
			 * [PT] LocalDateSerializer, LocalTimeSerializer e LocalDateTimeSerializer configuram um serializador
			 * personalizado StdSerializer do Jackson e DateTimeFormatter.ofPattern formata os tipos.
			 */
			jacksonObjectMapperBuilder
			.serializers( new LocalDateSerializer( DateTimeFormatter.ofPattern(dateFormat)))
			.deserializers( new LocalDateDeserializer( DateTimeFormatter.ofPattern(dateFormat)))
			.serializers( new LocalTimeSerializer( DateTimeFormatter.ofPattern(timeFormat)))
			.deserializers( new LocalTimeDeserializer( DateTimeFormatter.ofPattern(timeFormat)))
			.serializers( new LocalDateTimeSerializer( DateTimeFormatter.ofPattern(dateTimeFormat)))
			.deserializers( new LocalDateTimeDeserializer( DateTimeFormatter.ofPattern(dateTimeFormat)));
		};

	} // Jackson2ObjectMapperBuilderCustomizer

}
