package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.dto.MechanicDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class MechanicMapperUnitTest {

    @Autowired
    private MechanicMapper mechanicMapper;

    @Configuration
    public static class Config {
        @Bean
        public MechanicMapper mechanicMapper() {
            return Mappers.getMapper(MechanicMapper.class);
        }
    }

    @Test
    public void whenConvertMechanicToDto_thenReturnAppropriateMechanicDto() {
        // given
        Mechanic mechanic = new Mechanic("1", "1", "1", BigDecimal.ONE);
        mechanic.setId(5L);

        // when
        MechanicDto mechanicDto = mechanicMapper.mechanicToMechanicDto(mechanic);

        // then
        checkConformity(mechanic, mechanicDto);
    }

    @Test
    public void whenConvertMechanicWithNullsToDto_thenReturnedDtoWillContainNulls() {
        // given
        Mechanic mechanic = new Mechanic(null, "1", "1", null);

        // when
        MechanicDto mechanicDto = mechanicMapper.mechanicToMechanicDto(mechanic);

        // then
        checkConformity(mechanic, mechanicDto);
    }

    @Test
    public void whenConvertMechanicDtoToMechanic_thenReturnAppropriateMechanic() {
        //given
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setId(4L);
        mechanicDto.setFirstName("12");
        mechanicDto.setMiddleName("32");
        mechanicDto.setLastName("645");
        mechanicDto.setHourlyPayment(BigDecimal.TEN);

        // when
        Mechanic mechanic = mechanicMapper.mechanicDtoToMechanic(mechanicDto);

        // then
        checkConformity(mechanic, mechanicDto);
    }

    @Test
    public void whenConvertMechanicDtoWithNullsToMechanic_thenReturnedMechanicWillContainNulls() {
        //given
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setFirstName("12");
        mechanicDto.setHourlyPayment(BigDecimal.TEN);

        // when
        Mechanic mechanic = mechanicMapper.mechanicDtoToMechanic(mechanicDto);

        // then
        checkConformity(mechanic, mechanicDto);
    }

    @Test
    public void whenConvertMechanicListToMechanicDtoList_thenReturnDtoList() {
        // given
        List<Mechanic> mechanicList = Arrays.asList(
                new Mechanic("1", "1", "1", BigDecimal.ONE),
                new Mechanic("2", "2", "2", BigDecimal.ONE),
                new Mechanic("3", "3", "3", BigDecimal.ONE),
                new Mechanic("4", "4", "4", BigDecimal.ONE),
                new Mechanic("5", "5", "5", BigDecimal.ONE),
                new Mechanic("6", "6", "6", BigDecimal.ONE)
        );

        // when
        List<MechanicDto> mechanicDtoList = mechanicMapper.mechanicsToMechanicDtoList(mechanicList);

        // then
        for (int i = 0; i < mechanicList.size(); i++) {
            checkConformity(mechanicList.get(i), mechanicDtoList.get(i));
        }
    }

    @Test
    public void whenConvertNullMechanic_thenReturnNullDto() {
        assertThat(mechanicMapper.mechanicToMechanicDto(null)).isNull();
    }

    @Test
    public void whenConvertNullMechanicDto_thenReturnNullMechanic() {
        assertThat(mechanicMapper.mechanicDtoToMechanic(null)).isNull();
    }

    @Test
    public void whenConvertNullMechanicList_thenReturnNullDtoList() {
        assertThat(mechanicMapper.mechanicsToMechanicDtoList(null)).isNull();
    }

    private void checkConformity(Mechanic mechanic, MechanicDto mechanicDto) {
        assertThat(mechanic.getId()).isEqualTo(mechanicDto.getId());
        assertThat(mechanic.getFirstName()).isEqualTo(mechanicDto.getFirstName());
        assertThat(mechanic.getMiddleName()).isEqualTo(mechanicDto.getMiddleName());
        assertThat(mechanic.getLastName()).isEqualTo(mechanicDto.getLastName());
        assertThat(mechanic.getHourlyPayment()).isEqualTo(mechanicDto.getHourlyPayment());
    }
}
