package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.*;
import com.tenniscourts.mock.MockTests;
import com.tenniscourts.schedules.*;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @Mock
    ScheduleService scheduleService;

    @Mock
    GuestService guestService;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Spy
    ReservationMapper reservationMapper =  Mappers.getMapper(ReservationMapper.class);

    @Spy
    GuestMapper guestMapper =  Mappers.getMapper(GuestMapper.class);

    @Spy
    ScheduleMapper scheduleMapper =  Mappers.getMapper(ScheduleMapper.class);

    @Test
    public void bookReservation() {

        GuestDTO expectedGuestDTO = MockTests.createGuestDTO();
        TennisCourtDTO expectedTennisCourtDTO = MockTests.createTennisCourtDTO();
        ScheduleDTO expectedScheduleDTO = MockTests.createScheduleDTO();
        Reservation expectedReservation = MockTests.createReservation();
        List<Reservation> reservations = new ArrayList<Reservation>();
        reservations.add(expectedReservation);
        CreateReservationRequestDTO createReservationRequestDTO = MockTests.createCreateReservationRequestDTO();

        Mockito.when(scheduleService.findSchedule(Mockito.anyLong())).thenReturn(expectedScheduleDTO);
        Mockito.when(guestService.findGuestById(Mockito.anyLong())).thenReturn(expectedGuestDTO);
        Mockito.when(reservationRepository.saveAndFlush(Mockito.any())).thenReturn(expectedReservation);

        ReservationDTO reservation = reservationService.bookReservation(createReservationRequestDTO);

        Assert.assertEquals(expectedTennisCourtDTO.getId(), reservation.getSchedule().getTennisCourt().getId());
        Assert.assertEquals(expectedScheduleDTO.getId(), reservation.getSchedule().getId());
        Assert.assertEquals(expectedReservation.getId(), reservation.getId());

    }

    @Test
    public void bookReservation_ScheduleAlreadyBooked() {

        GuestDTO expectedGuestDTO = MockTests.createGuestDTO();
        ScheduleDTO expectedScheduleDTO = MockTests.createScheduleDTO();
        Reservation expectedReservation = MockTests.createReservation();
        List<Reservation> reservations = new ArrayList<Reservation>();
        reservations.add(expectedReservation);
        CreateReservationRequestDTO createReservationRequestDTO = MockTests.createCreateReservationRequestDTO();

        Mockito.when(reservationRepository.findBySchedule_Id(Mockito.anyLong())).thenReturn(reservations);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            ReservationDTO reservation = reservationService.bookReservation(createReservationRequestDTO);
        });

        String expectedMessage = "Reservation/Schedule already booked.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

}