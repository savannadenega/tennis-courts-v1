package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.mock.MockTests;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ScheduleService.class)
public class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    TennisCourtService tennisCourtService;

    @InjectMocks
    ScheduleService scheduleService;

    @Spy
    TennisCourtMapper tennisCourtMapper = Mappers.getMapper(TennisCourtMapper.class);

    @Spy
    ScheduleMapper scheduleMapper = Mappers.getMapper(ScheduleMapper.class);

    @Test
    public void addSchedule() {

        TennisCourtDTO expectedTennisCourtDTO = MockTests.createTennisCourtDTO();
        CreateScheduleRequestDTO createScheduleRequestDTO = MockTests.createCreateScheduleRequestDTO();
        Mockito.when(tennisCourtService.findTennisCourtById(Mockito.anyLong())).thenReturn(expectedTennisCourtDTO);

        ScheduleDTO scheduleDTO = scheduleService.addSchedule(expectedTennisCourtDTO.getId(), createScheduleRequestDTO);

        assertEquals(expectedTennisCourtDTO.getId(), scheduleDTO.getTennisCourt().getId());
        assertNotNull(scheduleDTO.getStartDateTime());
        assertNotNull(scheduleDTO.getEndDateTime());

    }

    @Test
    public void addSchedule_ScheduleAlreadyBookedForTennisCourt() {

        TennisCourtDTO expectedTennisCourtDTO = MockTests.createTennisCourtDTO();
        CreateScheduleRequestDTO createScheduleRequestDTO = MockTests.createCreateScheduleRequestDTO();
        Mockito.when(tennisCourtService.findTennisCourtWithSchedulesById(Mockito.anyLong())).thenReturn(expectedTennisCourtDTO);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            ScheduleDTO scheduleDTO = scheduleService.addSchedule(expectedTennisCourtDTO.getId(), createScheduleRequestDTO);
        });

        String expectedMessage = "Schedule already booked for Tennis Court.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void findSchedule() {

        Schedule schedule = MockTests.createSchedule();
        ScheduleDTO expectedScheduleDTO = MockTests.createScheduleDTO();
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(schedule));

        ScheduleDTO scheduleDTO = scheduleService.findSchedule(schedule.getId());

        assertEquals(expectedScheduleDTO.getId(), scheduleDTO.getId());
        assertNotNull(scheduleDTO.getStartDateTime());
        assertNotNull(scheduleDTO.getEndDateTime());
        assertEquals(expectedScheduleDTO.getStartDateTime(), scheduleDTO.getStartDateTime());
        assertEquals(expectedScheduleDTO.getEndDateTime(), scheduleDTO.getEndDateTime());

    }

    @Test
    public void findSchedule_ScheduleNotFound() {

        Schedule schedule = MockTests.createSchedule();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            ScheduleDTO scheduleDTO = scheduleService.findSchedule(schedule.getId());
        });

        String expectedMessage = "Schedule not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }
}