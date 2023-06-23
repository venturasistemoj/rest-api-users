package com.venturasistemoj.restapi.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;

/**
 * [EN] Integration test class for user management RESTful API using Spring Boot framework and the TestRestTemplate
 * library to perform HTTP requests and check responses.
 *
 * The @SpringBootTest annotation can be specified in a test class that runs Spring Boot based tests. In addition to the
 * regular Spring TestContextFramework, it provides support for different web environment modes, including the ability
 * to start a running web server listening on a defined or random port, among others. Can register the following beans
 * for web tests that are using a running web server:TestRestTemplate, WebTestClient and HttpGraphQlTester.
 *
 * TestRestTemplate provides a more realistic way to test HTTP requests. She starts an embedded server and
 * makes real HTTP calls to the running API. Allows you to test the integration between various system components,
 * including controllers, service layer and persistence layer. Using TestRestTemplate is more suitable
 * for integration tests that span multiple layers of the system.
 *
 * [PT] Classe de testes de integração para a API RESTful de gerenciamento de usuários que utiliza o framework
 * Spring Boot e a biblioteca TestRestTemplate para realizar as requisições HTTP e verificar as respostas.
 *
 * A anotação @SpringBootTest pode ser especificada em uma classe de teste que executa testes baseados em Spring Boot.
 * Além do Spring TestContextFramework regular, fornece suporte para diferentes modos de ambiente web, incluindo a
 * capacidade de iniciar um servidor web em execução escutando em uma porta definida ou aleatória, entre outros.
 * Pode registrar os seguintes beans para testes web que estão usando um servidor web em execução:
 * TestRestTemplate, WebTestClient e HttpGraphQlTester.
 *
 * TestRestTemplate oferece uma maneira mais realista de testar requisições HTTP. Ele inicia um servidor incorporado e
 * faz chamadas HTTP reais para a API em execução. Permite testar a integração entre vários componentes do sistema,
 * incluindo controladores, camada de serviço e camada de persistência. A utilização de TestRestTemplate é mais adequada
 * para testes de integração que abrangem várias camadas do sistema.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringUserTests {

	// URL base da API
	private static final String URL_API = "/rest-api/users";

	@Autowired private TestRestTemplate restTemplate;
	@Autowired private UserService userService;

	private UserDTO user;
	private UserDTO savedUser;

	// Configuração inicial para os testes
	@BeforeEach
	public void setUp() {

		// Criação de um usuário usando o padrão de design Builder por meio do Lombok
		user = UserDTO.builder()
				.userId(1L)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();

		/*
		// Criação de um endereço para o usuário
		AddressDTO addressTest = AddressDTO.builder()
				.addressId(1L)
				.publicPlace("Avenida")
				.streetAddress("Glasshouse, 69")
				.complement("1001")
				.city("Rio 40º")
				.state("RJ")
				.zipCode("69.069-069")
				.build();

		user.setAddressDTO(addressTest);
		addressTest.setUserDTO(user);

		// Criação de um número de telefone para o usuário
		PhoneNumberDTO phone = PhoneNumberDTO.builder()
				.phoneId(1L)
				.type("Cel")
				.number("(21) 96687-8776")
				.build();

		List<PhoneNumberDTO> phoneTest = new ArrayList<>();
		phoneTest.add(phone);
		user.setPhonesDTO(phoneTest);

		phone.setUserDTO(user);
		 */

		// Salva o usuário no repositório para uso nos testes
		savedUser = userService.createUser(user);
	}

	@Test
	public void testCreateUser() {

		// Envia uma requisição POST para a URL_API com o usuário no corpo da requisição
		ResponseEntity<UserDTO> response = restTemplate.postForEntity(URL_API, user, UserDTO.class);

		// Verifica o status da resposta e os dados do usuário retornados
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getUserId());
		assertEquals(user.getName(), response.getBody().getName());
		assertEquals(user.getEmail(), response.getBody().getEmail());
		assertEquals(user.getCpf(), response.getBody().getCpf());
		assertEquals(user.getBirthDate(), response.getBody().getBirthDate());
		assertEquals(user.getAddressDTO(), response.getBody().getAddressDTO());

		/*
		// Verifica se os conjuntos têm o mesmo tamanho
		assertEquals(user.getPhonesDTO().size(), response.getBody().getPhonesDTO().size());

		// Verifica com Java 8 se os elementos esperados estão presentes no conjunto atual.
		// Mesma função do código comentado acima (Java 7)
		boolean allPhonesMatched = response.getBody().getPhonesDTO().stream()
				.allMatch(expectedPhone -> user.getPhonesDTO().stream()
						.anyMatch(actualPhone -> expectedPhone.getType().equals(actualPhone.getType()) &&
								expectedPhone.getNumber().equals(actualPhone.getNumber())));

		Assertions.assertTrue(allPhonesMatched);
		 */
	}

	@Test
	public void testGetUser() {

		// Envia uma requisição GET para a URL_API com o ID do usuário
		ResponseEntity<UserDTO> response = restTemplate.getForEntity(URL_API + "/" + savedUser.getUserId(), UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(savedUser.getUserId(), response.getBody().getUserId());
		assertEquals(savedUser.getName(), response.getBody().getName());
		assertEquals(savedUser.getEmail(), response.getBody().getEmail());
		assertEquals(savedUser.getCpf(), response.getBody().getCpf());
		assertEquals(savedUser.getBirthDate(), response.getBody().getBirthDate());
		assertEquals(savedUser.getAddressDTO(), response.getBody().getAddressDTO());
		assertEquals(savedUser.getPhonesDTO(), response.getBody().getPhonesDTO());

	}

	@Test
	public void testGetUsers() {

		// Envia uma requisição GET para a URL_API para buscar todos os usuários
		ResponseEntity<List<UserDTO>> response = restTemplate.exchange(URL_API, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDTO>>() {
		});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertEquals(savedUser.getUserId(), response.getBody().get(0).getUserId());
		assertEquals(savedUser.getName(), response.getBody().get(0).getName());
		assertEquals(savedUser.getEmail(), response.getBody().get(0).getEmail());
		assertEquals(savedUser.getCpf(), response.getBody().get(0).getCpf());
		assertEquals(savedUser.getBirthDate(), response.getBody().get(0).getBirthDate());
		assertEquals(savedUser.getAddressDTO(), response.getBody().get(0).getAddressDTO());
		assertEquals(savedUser.getPhonesDTO(), response.getBody().get(0).getPhonesDTO());
	}

	@Test
	public void testUpdateUser() {

		// Cria um novo usuário com os dados atualizados
		user = UserDTO.builder()
				.userId(1L)
				.name("Dilma")
				.surName("Rousseff")
				.birthDate(LocalDate.of(1956, Month.OCTOBER, 26))
				.cpf("789.456.123-10")
				.email("dilmae@prov.com")
				.build();

		// Cria uma entidade HTTP com o usuário atualizado
		HttpEntity<UserDTO> requestUpdate = new HttpEntity<>(user);

		// Envia uma requisição PUT para a URL_API com o ID do usuário e a entidade HTTP atualizada
		ResponseEntity<UserDTO> response = restTemplate.exchange(
				URL_API + "/" + 1L, HttpMethod.PUT, requestUpdate, UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(user.getUserId(), response.getBody().getUserId());
		assertEquals(user.getName(), response.getBody().getName());
		assertEquals(user.getEmail(), response.getBody().getEmail());
		assertEquals(user.getCpf(), response.getBody().getCpf());
		assertEquals(user.getBirthDate(), response.getBody().getBirthDate());
	}

	@Test
	public void testDeleteUser() {

		/* Envia uma requisição DELETE para a URL_API com o ID do usuário. A classe Void é uma classe de espaço
		 * reservado não instanciável para conter uma referência ao objeto Class que representa a palavra-chave Java void.
		 */
		ResponseEntity<Void> response = restTemplate.exchange(
				URL_API + "/" + savedUser.getUserId(), HttpMethod.DELETE, null, Void.class);

		// Verifica o status da resposta
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

	}

}
