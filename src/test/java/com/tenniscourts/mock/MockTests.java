package com.tenniscourts.mock;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.reservations.CreateReservationRequestDTO;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.reservations.ReservationStatus;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

public class MockTests {

    public static GuestDTO createGuestDTO(){
        return new GuestDTO(1L, "Guest #1");
    }

    public static Guest createGuest(){
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setName("Guest #1");
        return guest;
    }

    public static TennisCourtDTO createTennisCourtDTO(){
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        tennisCourtDTO.setId(1L);
        tennisCourtDTO.setName("Tennis Court #1");
        return tennisCourtDTO;
    }

    public static TennisCourt createTennisCourt(){
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setId(1L);
        tennisCourt.setName("Tennis Court #1");
        return tennisCourt;
    }

    public static ScheduleDTO createScheduleDTO(){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1L);
        LocalDateTime localDateTime = LocalDateTime.of(2021,
                Month.APRIL, 15, 22, 00, 00);
        scheduleDTO.setStartDateTime(localDateTime);
        scheduleDTO.setEndDateTime(localDateTime.plusHours(1));
        scheduleDTO.setTennisCourt(createTennisCourtDTO());
        return scheduleDTO;
    }

    public static Schedule createSchedule(){
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        LocalDateTime localDateTime = LocalDateTime.of(2021,
                Month.APRIL, 15, 22, 00, 00);
        schedule.setStartDateTime(localDateTime);
        schedule.setEndDateTime(localDateTime.plusHours(1));
        schedule.setTennisCourt(createTennisCourt());
        return schedule;
    }

    public static Reservation createReservation(){
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuest(createGuest());
        reservation.setSchedule(createSchedule());
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        reservation.setRefundValue(BigDecimal.valueOf(10.00));
        reservation.setValue(BigDecimal.valueOf(25.00));
        return reservation;
    }

    public static CreateReservationRequestDTO createCreateReservationRequestDTO(){
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(1L);
        createReservationRequestDTO.setScheduleId(1L);
        return createReservationRequestDTO;
    }

    public static CreateScheduleRequestDTO createCreateScheduleRequestDTO(){
        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setTennisCourtId(createTennisCourt().getId());
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.of(2021,
                Month.APRIL, 15, 22, 00, 00));
        return createScheduleRequestDTO;
    }

}
