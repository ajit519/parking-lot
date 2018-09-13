package com.gojek.parking.base;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseServiceTest {

    protected static final String REG_1 = "KA-01-HH-1234";
    protected static final String REG_2 = "KA-01-HH-9999";
    protected static final String REG_3 = "KA-01-BB-0001";
    protected static final String REG_4 = "KA-01-HH-2701";
    protected static final String REG_5 = "KA-01-HA-2701";
    protected static final String REG_6 = "KA-01-HH-3141";
    protected static final String REG_7 = "KA-01-HH-4141";


    protected static final String WHITE = "White";
    protected static final String BLACK = "Black";
    protected static final String BLUE = "Blue";

    protected static final int CAPACITY = 6;

    protected static final int SLOT_1 = 1;
    protected static final int SLOT_2 = 2;
    protected static final int SLOT_3 = 3;
    protected static final int SLOT_4 = 4;
    protected static final int SLOT_5 = 5;
    protected static final int SLOT_6 = 6;

}
