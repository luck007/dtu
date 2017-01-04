package com.dtu.android.events;

import com.squareup.otto.Bus;

public class DtuBus extends Bus {
    public static final DtuBus main;

    static {
        main = new DtuBus();
    }
}
