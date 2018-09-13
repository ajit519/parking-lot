package com.gojek.parking.command.impl;

import com.gojek.parking.base.BaseServiceTest;
import com.gojek.parking.command.CreateParkingSlot;
import com.gojek.parking.entity.Slot;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class CreateParkingSlotImplTest extends BaseServiceTest {

    private CreateParkingSlot createParkingSlot;
    private static final int CAPACITY = 6;

    @Before
    public void setUp() {
      createParkingSlot = new CreateParkingSlotImpl();

    }

    @Test
    public void givenSlotCapacityIsPositive_whenInitializeSlot_thenSuccess() {
        createParkingSlot.initializeParkingSlot(CAPACITY);
        Set<Slot> slots = createParkingSlot.getSlots();

        assertEquals(slots.size(), CAPACITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSlotCapacityIsNegative_whenInitializeSlot_thenReturnError(){
        createParkingSlot.initializeParkingSlot(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSlotCapacityIsZero_whenInitializeSlot_thenReturnError(){
        createParkingSlot.initializeParkingSlot(0);
    }


}