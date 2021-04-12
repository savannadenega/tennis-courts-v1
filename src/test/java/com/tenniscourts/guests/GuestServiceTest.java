package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.mock.MockTests;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
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
}