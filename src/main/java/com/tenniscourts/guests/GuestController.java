package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @ApiOperation(value = "Add Guest")
    public ResponseEntity<Void> addGuest(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find Guest by Id")
    public ResponseEntity<GuestDTO> findById(Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping("/by-name")
    @ApiOperation(value = "Find Guest by Name")
    public ResponseEntity<List<GuestDTO>> findByName(String name) {
        return ResponseEntity.ok(guestService.findGuestByName(name));
    }

    @GetMapping
    @ApiOperation(value = "Find All Guest")
    public ResponseEntity<List<GuestDTO>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update Guest")
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable("id") Long guestId, @RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete Guest")
    public ResponseEntity<Void> deleteGuest(Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
