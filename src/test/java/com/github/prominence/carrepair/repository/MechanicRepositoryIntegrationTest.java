package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.domain.Mechanic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class MechanicRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Test
    public void whenFindById_thenReturnClient() {
        // given
        Mechanic mechanic = new Mechanic("firstName", "middleName", "lastName", BigDecimal.valueOf(12));
        entityManager.persist(mechanic);
        entityManager.flush();

        // when
        Optional<Mechanic> found = mechanicRepository.findById(mechanic.getId());

        // then
        assertThat(found).isPresent().hasValue(mechanic)
                .map(Mechanic::hashCode).get().isEqualTo(mechanic.hashCode());
    }

    @Test
    public void whenFindByWrongId_thenReturnNull() {
        // when
        Optional<Mechanic> found = mechanicRepository.findById(-5L);

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    public void whenFindByInitials_thenReturnList() {
        // given
        Stream.of(
                new Mechanic("Alexey", "MiddleName", "Zinchenko", BigDecimal.valueOf(12)),
                new Mechanic("John", "MiddleName", "Smith", BigDecimal.valueOf(25)),
                new Mechanic("Dirk", "Another", "Surname", BigDecimal.valueOf(73))
        ).forEach(entityManager::persist);
        entityManager.flush();

        // when
        List<Mechanic> mechanicsByMiddleName = mechanicRepository.findAllByInitials("Name");

        // then
        assertThat(mechanicsByMiddleName).size().isEqualTo(2);

        // when
        List<Mechanic> mechanicsByNonExistingInfo = mechanicRepository.findAllByInitials("Ivan");

        // then
        assertThat(mechanicsByNonExistingInfo).isEmpty();

        // when
        List<Mechanic> clientsByName = mechanicRepository.findAllByInitials("Alexey");

        // then
        assertThat(clientsByName).isNotEmpty().size().isEqualTo(1);
    }
}
