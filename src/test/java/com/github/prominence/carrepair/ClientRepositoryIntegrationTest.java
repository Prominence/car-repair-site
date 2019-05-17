package com.github.prominence.carrepair;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ClientRepository clientRepository;

	@Test
	public void whenFindById_thenReturnClient() {
		// given
		Client client = new Client("firstName", "middleName", "lastName", "123456789");
		entityManager.persist(client);
		entityManager.flush();

		// when
		Client found = clientRepository.findById(client.getId()).get();

		// then
		assertThat(found.hashCode()).isEqualTo(client.hashCode());
	}

}

