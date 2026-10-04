package io.github.jfglzs.asa.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class Hardware {
    public static final SystemInfo info;
    public static final HardwareAbstractionLayer hardware;
    public static final CentralProcessor processor;

    static {
        info = new SystemInfo();
        hardware = info.getHardware();
        processor = hardware.getProcessor();
    }
}
