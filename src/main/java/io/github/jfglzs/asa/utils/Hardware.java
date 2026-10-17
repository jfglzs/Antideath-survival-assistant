package io.github.jfglzs.asa.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class Hardware {
    public static final SystemInfo INFO;
    public static final HardwareAbstractionLayer HARD_WARE;
    public static final CentralProcessor PROCESSOR;

    static {
        INFO = new SystemInfo();
        HARD_WARE = INFO.getHardware();
        PROCESSOR = HARD_WARE.getProcessor();
    }
}
