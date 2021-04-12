package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.mock.MockTests;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TennisCourtService.class)
public class TennisCourtServiceTest {

    @Mock
    TennisCourtRepository tennisCourtRepository;

    @Mock
    ScheduleService scheduleService;

    @InjectMocks
    TennisCourtService tennisCourtService;

    @Spy
    ScheduleMapper scheduleMapper =  Mappers.getMapper(ScheduleMapper.class);

    @Spy
    TennisCourtMapper tennisCourtMapper =  Mappers.getMapper(TennisCourtMapper.class);

    @Test
    public void findTennisCourtById() {

        TennisCourt expectedTennisCourt = MockTests.createTennisCourt();
        Mockito.when(tennisCourtRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expectedTennisCourt));

        TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtById(1L);

        assertEquals(expectedTennisCourt.getId(), tennisCourtDTO.getId());
    }

    @Test
    public void findTennisCourtById_TennisCourtNotFound() {

        TennisCourt expectedTennisCourt = MockTests.createTennisCourt();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtById(expectedTennisCourt.getId());
        });

        String expectedMessage = "Tennis Court not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void findTennisCourtWithSchedulesById() {

        TennisCourt expectedTennisCourt = MockTests.createTennisCourt();
        ScheduleDTO expectedScheduleDTO = MockTests.createScheduleDTO();
        List<ScheduleDTO> expectedScheduleDTOList = new ArrayList<>();
        expectedScheduleDTOList.add(expectedScheduleDTO);

        Mockito.when(tennisCourtRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expectedTennisCourt));
        Mockito.when(scheduleService.findSchedulesByTennisCourtId(Mockito.anyLong())).thenReturn(expectedScheduleDTOList);

        TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtWithSchedulesById(expectedTennisCourt.getId());

        assertEquals(expectedTennisCourt.getId(), tennisCourtDTO.getId());
    }

}