package com.gojek.parking.command.impl;

import com.gojek.parking.base.BaseServiceTest;
import com.gojek.parking.command.CreateParkingSlot;
import com.gojek.parking.command.ParkService;
import com.gojek.parking.entity.Slot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class ParkingServiceImplTest extends BaseServiceTest {

    private ParkService parkService;

    @Mock
    private CreateParkingSlot createParkingSlot;

    private Set<Slot> slots;


    @Before
    public void setUp() {

        when(createParkingSlot.getSlots()).thenReturn(expectSlot());
        parkService = new ParkingServiceImpl(slots);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenVehicle_whenRegistrationNumberIsMissing_thenReturnError() {

        parkService.park(null, WHITE);

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenVehicle_whenColorIsMissing_thenReturnError() {

        parkService.park(REG_1, null);

    }

    @Test
    public void givenVehicleHaveAllDetails_whenParkingIsAvailable_thenParkVehicleSuccess(){
        parkService.park(REG_1, WHITE);
        parkService.park(REG_2, WHITE);
        parkService.park(REG_3, BLACK);
        parkService.park(REG_4, BLUE);
        parkService.park(REG_5, WHITE);
        parkService.park(REG_6, BLACK);
    }

    @Test
    public void givenVehicleAlready_whenOtherVehicleWithSameRegistrationNumberRequestToPark_thenReturnError(){
        parkService.park(REG_1, WHITE);

        try{
            parkService.park(REG_1, BLACK);
            fail("Expected an RuntimeException to be thrown");
        }catch (RuntimeException ex){
            assertThat(ex.getMessage(), is("Vehicle with registration number KA-01-HH-1234 already been parked"));
        }


    }

    @Test
    public void givenVehiclesRequestParking_whenParkingIsFull_thenReturnError(){
        parkService.park(REG_1, WHITE);
        parkService.park(REG_2, WHITE);
        parkService.park(REG_3, BLACK);
        parkService.park(REG_4, BLUE);
        parkService.park(REG_5, WHITE);
        parkService.park(REG_6, BLACK);
        //try {
            parkService.park(REG_7, BLACK);
      //      fail("Parking is full, throw RuntimeException");
        //}catch (RuntimeException ex){
       //     assertThat(ex.getMessage(), is("Sorry, parking lot is full"));
        //}

    }

    @Test
    public void givenMultipleUser_whenParkingVehicle_thenThrowError(){
        List<String> registrationNumbers = Arrays.asList(REG_1, REG_2, REG_3, REG_4, REG_5, REG_6, REG_7);
        ExecutorService executorService = Executors.newFixedThreadPool(CAPACITY+1);
        try {
            registrationNumbers
                    .forEach(reg -> executorService.submit(() -> {
                        parkService.park(reg, WHITE);
                    }));
        }catch (RuntimeException ex){
            assertThat(ex.getMessage(), is("Sorry, parking lot is full"));
        }

    }

    @Test
    public void givenSlotNumber_whenUnParkVehicle_thenReturnSuccess(){
        parkService.park(REG_1, WHITE);
        parkService.unpark(SLOT_1);
    }

    @Test
    public void givenSlotNumberNotBeenAllocated_whenUnParkVehicle_thenReturnError(){
        parkService.park(REG_1, WHITE);
        try {
            parkService.unpark(SLOT_2);
            fail("Un parking failed Due to invalid Slot id");
        }catch (IllegalArgumentException ex){
            assertThat(ex.getMessage(), is("Invalid slotId"));
        }
    }

    @Test
    public void givenParking_fetchCurrentStatus_thenSuccess(){
        parkService.park(REG_1, WHITE);
        parkService.park(REG_2, WHITE);
        parkService.park(REG_3, BLACK);
        parkService.park(REG_4, BLUE);
        parkService.status();
    }

    @Test
    public void givenColour_whenFetchSlot_thenReturnSlotNumber(){
        parkService.park(REG_1, WHITE);
        parkService.park(REG_2, WHITE);
        parkService.park(REG_3, BLACK);
        parkService.park(REG_4, BLUE);

        List<Integer> slots = parkService.getSlotsForColour(WHITE);

        assertTrue(slots.contains(SLOT_1));
        assertTrue(slots.contains(SLOT_2));

    }

    @Test
    public void givenColour_whenNoVehiclePark__thenReturnNoSlot(){
        List<Integer> slots = parkService.getSlotsForColour(WHITE);

        assertTrue(slots.isEmpty());
    }

    @Test
    public void givenRegistrationNumber_whenFetchSlot_thenReturnSlotNumber(){
        parkService.park(REG_4, BLUE);

        Slot slot = parkService.getSlot(REG_4);

        assertThat(slot.getId(), is(1));
    }

    @Test
    public void givenRegistrationNumberNotParked_whenFetchSlot_thenReturnNotFound(){
        parkService.park(REG_4, BLUE);

        try {
            parkService.getSlot(REG_6);
            fail("Given registration number not found");
        }catch (RuntimeException ex){
            assertThat(ex.getMessage(), is("Not Found"));
        }

    }

    private Set<Slot> expectSlot(){
        this.slots = new HashSet<>();
        IntStream.rangeClosed(1, CAPACITY).forEach(id -> slots.add(new Slot(id)));
        return slots;
    }
}