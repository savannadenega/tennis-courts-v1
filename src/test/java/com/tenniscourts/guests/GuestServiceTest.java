package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.mock.MockTests;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {

    @Mock
    GuestRepository guestRepository;

    @InjectMocks
    GuestService guestService;

    @Spy
    GuestMapper guestMapper = Mappers.getMapper(GuestMapper.class);

    @Test
    public void addGuest() {

        Guest expectedGuest = MockTests.createGuest();
        Mockito.when(guestRepository.saveAndFlush(Mockito.any())).thenReturn(expectedGuest);

        GuestDTO expectedGuestDTO = new GuestDTO();
        expectedGuestDTO.setId(1L);
        expectedGuestDTO.setName("Guest #1");
        GuestDTO guestDTO = guestService.addGuest(expectedGuestDTO);

        assertEquals(expectedGuestDTO.getId(), guestDTO.getId());
        assertEquals(expectedGuestDTO.getName(), guestDTO.getName());

    }

    @Test
    public void updateGuest() {

        Guest expectedGuest = MockTests.createGuest();
        Mockito.when(guestRepository.saveAndFlush(Mockito.any())).thenReturn(expectedGuest);

        GuestDTO expectedGuestDTO = new GuestDTO();
        expectedGuestDTO.setId(1L);
        expectedGuestDTO.setName("Guest #1");
        GuestDTO guestDTO = guestService.updateGuest(expectedGuestDTO);

        assertEquals(expectedGuestDTO.getId(), guestDTO.getId());
        assertEquals(expectedGuestDTO.getName(), guestDTO.getName());

    }

    @Test
    public void deleteGuest() {

        GuestDTO expectedGuestDTO = new GuestDTO();
        expectedGuestDTO.setId(1L);
        expectedGuestDTO.setName("Guest #1");
        guestService.deleteGuest(expectedGuestDTO.getId());

        Mockito.verify(guestRepository).deleteById(expectedGuestDTO.getId());

    }

    @Test
    public void findGuestById() {

        Guest expectedGuest = MockTests.createGuest();
        expectedGuest.setId(1L);
        Mockito.when(guestRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expectedGuest));

        GuestDTO guestDTO = guestService.findGuestById(expectedGuest.getId());

        assertEquals(expectedGuest.getId(), guestDTO.getId());
        assertEquals(expectedGuest.getName(), guestDTO.getName());

    }

    @Test
    public void findGuestById_GuestNotFound() {

        Guest expectedGuest = MockTests.createGuest();
        expectedGuest.setId(1L);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            GuestDTO guestDTO = guestService.findGuestById(expectedGuest.getId());
        });

        String expectedMessage = "Guest not found.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void findGuestByName() {

        Guest expectedGuest = MockTests.createGuest();
        List<Guest> expectedGuestList = new ArrayList<>();
        expectedGuestList.add(expectedGuest);
        Mockito.when(guestRepository.findByNameContains(Mockito.anyString())).thenReturn(expectedGuestList);

        List<GuestDTO> guestDTO = guestService.findGuestByName(expectedGuest.getName());

        assertEquals(expectedGuest.getId(), guestDTO.get(0).getId());
        assertEquals(expectedGuest.getName(), guestDTO.get(0).getName());

    }

    @Test
    public void findAll() {

        Guest expectedGuest = MockTests.createGuest();
        List<Guest> expectedGuestList = new ArrayList<>();
        expectedGuestList.add(expectedGuest);
        Mockito.when(guestRepository.findAll()).thenReturn(expectedGuestList);

        List<GuestDTO> guestDTO = guestService.findAll();

        assertEquals(expectedGuest.getId(), guestDTO.get(0).getId());
        assertEquals(expectedGuest.getName(), guestDTO.get(0).getName());

    }
}