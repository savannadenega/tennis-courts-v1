package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final TennisCourtService tennisCourtService;
    private final TennisCourtMapper tennisCourtMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourtDTO tennisCourtWithSchedulesById = tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId);
        if(Objects.nonNull(tennisCourtWithSchedulesById)){
            throw new EntityNotFoundException("Schedule already booked for Tennis Court.");
        }

        TennisCourtDTO tennisCourt = tennisCourtService.findTennisCourtById(tennisCourtId);
        Schedule schedule = scheduleMapper.map(createScheduleRequestDTO);
        schedule.setTennisCourt(tennisCourtMapper.map(tennisCourt));
        schedule.setEndDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1));
        return scheduleMapper.map(schedule);
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findAll() {
        return scheduleMapper.map(scheduleRepository.findAll());
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
