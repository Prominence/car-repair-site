package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.dto.MechanicDto;
import com.github.prominence.carrepair.model.mapper.MechanicMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(MechanicService.class)
public class MechanicServiceIntegrationTest {

    @Autowired
    private MechanicService mechanicService;

    @MockBean
    private MechanicMapper mechanicMapper;

    @Before
    public void setup() {
        Arrays.asList(
                new Mechanic("1", "1", "1", BigDecimal.ONE),
                new Mechanic("2", "2", "2", BigDecimal.TEN),
                new Mechanic("3", "3", "3", BigDecimal.ONE.add(BigDecimal.ONE))
        ).forEach(mechanicService::save);
    }

    @Test
    public void whenSaveValidMechanic_thenHeWillBePersisted() {
        // given
        Mechanic testMechanic = getTestMechanic();

        // when
        testMechanic = mechanicService.save(testMechanic);

        // then
        assertThat(testMechanic.getId()).isPositive();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidMechanic_thenThrowAnException() {
        // given
        Mechanic testMechanic = getTestMechanic();
        testMechanic.setFirstName(null);

        // expected exception
        mechanicService.save(testMechanic);
    }

    @Test
    public void whenSaveMechanicDto_thenConvertToOMechanicAndSave() {
        // given
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setFirstName("1");
        mechanicDto.setMiddleName("1");
        mechanicDto.setLastName("1");
        mechanicDto.setHourlyPayment(BigDecimal.ONE);

        when(mechanicMapper.mechanicDtoToMechanic(mechanicDto))
                .thenReturn(new Mechanic("1", "1", "1", BigDecimal.ONE));

        // when
        mechanicService.save(mechanicDto);

        // then
        verify(mechanicMapper, times(1)).mechanicDtoToMechanic(mechanicDto);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidMechanicDto_thenThrowAnException() {
        // given
        MechanicDto mechanicDto = new MechanicDto();

        when(mechanicMapper.mechanicDtoToMechanic(mechanicDto))
                .thenReturn(new Mechanic());

        // expected exception
        mechanicService.save(mechanicDto);
    }

    @Test
    public void whenFindAll_thenReturnAllSavedMechanics() {
        assertThat(mechanicService.findAll(PageRequest.of(0, 10))).isNotEmpty();
    }

    @Test
    public void whenFindById_thenReturnMechanicWithThisId() {
        // given
        Mechanic testMechanic = getTestMechanic();

        // when
        Long testMechanicId = mechanicService.save(testMechanic).getId();
        Mechanic returnedMechanic = mechanicService.findById(testMechanicId).get();

        // then
        assertThat(returnedMechanic).isNotNull();
        assertThat(returnedMechanic.getId()).isEqualTo(testMechanicId);
    }

    @Test
    public void whenGetMechanicCount_thenReturnPositiveMechanicCount() {
        // when
        Long mechanicCount = mechanicService.getMechanicCount();

        // then
        assertThat(mechanicCount).isPositive();
    }

    @Test
    public void whenNoSavedMechanicsAndGetMechanicCount_thenReturnZero() {
        // given
        mechanicService.deleteAll();

        // when
        Long mechanicCount = mechanicService.getMechanicCount();

        // then
        assertThat(mechanicCount).isZero();
    }

    @Test
    public void whenSearchByInitials_thenReturnAllThatMatches() {
        // given
        Arrays.asList(
                new Mechanic("John", "1", "Smith", BigDecimal.ONE),
                new Mechanic("Ivan", "2", "Ivanov", BigDecimal.TEN),
                new Mechanic("Alexey", "Ivanovich", "Smith", BigDecimal.TEN.add(BigDecimal.ONE))
        ).forEach(mechanicService::save);

        // when
        List<Mechanic> foundMechanics = mechanicService.searchByInitials("Ivan");

        // then
        assertThat(foundMechanics.size()).isEqualTo(2);

        // when
        foundMechanics = mechanicService.searchByInitials("Smith");

        // then
        assertThat(foundMechanics.size()).isEqualTo(2);

        // when
        foundMechanics = mechanicService.searchByInitials("Jo");

        // then
        assertThat(foundMechanics.size()).isEqualTo(1);

        // when
        foundMechanics = mechanicService.searchByInitials("noone");

        // then
        assertThat(foundMechanics.size()).isZero();
    }

    @Test
    public void whenDeleteMechanicById_thenHeWillBeDeleted() {
        // given
        Mechanic testMechanic = getTestMechanic();

        // when
        Long savedMechanicId = mechanicService.save(testMechanic).getId();
        boolean wasDeleted = mechanicService.deleteMechanicById(savedMechanicId);

        // then
        assertThat(wasDeleted).isTrue();
        assertThat(mechanicService.findById(savedMechanicId).isPresent()).isFalse();
    }

    @Test
    public void whenConvertToDto_thenMapperIsParticipating() {
        // given
        Mechanic testMechanic = getTestMechanic();

        // when
        mechanicService.convertToDto(testMechanic);

        // then
        verify(mechanicMapper, times(1)).mechanicToMechanicDto(testMechanic);
    }

    @Test
    public void whenConvertMechanicPageToDtoPage_thenMapperIsParticipating() {
        // given
        Page<Mechanic> mechanicPage = mechanicService.findAll(PageRequest.of(0, 10));

        // when
        mechanicService.convertToDtoPage(mechanicPage);

        // then
        verify(mechanicMapper, times(1)).mechanicsToMechanicDtoList(mechanicPage.getContent());
    }

    @Test
    public void whenConvertMechanicListToDtoList_thenMapperIsParticipating() {
        // given
        List<Mechanic> mechanics = Collections.singletonList(getTestMechanic());

        // when
        mechanicService.convertToDtoList(mechanics);

        // then
        verify(mechanicMapper, times(1)).mechanicsToMechanicDtoList(mechanics);
    }

    private Mechanic getTestMechanic() {
        return new Mechanic("firstName", "middleName", "lastName", BigDecimal.ONE.add(BigDecimal.ONE));
    }
}
