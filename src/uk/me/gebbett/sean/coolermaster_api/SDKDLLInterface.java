package uk.me.gebbett.sean.coolermaster_api;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;


public interface SDKDLLInterface extends Library {
    SDKDLLInterface INSTANCE = Native.load("SDKDLL", SDKDLLInterface.class);

    // === Constants ===

    int MAX_LED_ROW = 7;
    int MAX_LED_COLUMN = 24;


    // === Structs ===

    @Structure.FieldOrder({"r", "g", "b"})
    class KEY_COLOR extends Structure {
        public KEY_COLOR(byte r, byte g, byte b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        public KEY_COLOR() {
            this.r = 0;
            this.g = 0;
            this.b = 0;
        }
        public byte r;
        public byte g;
        public byte b;
    }

    @Structure.FieldOrder({"KeyColor"})
    class COLOR_MATRIX extends Structure {
        public final int KEY_COLOR_OUTER_LEN = MAX_LED_ROW;
        public final int KEY_COLOR_INNER_LEN = MAX_LED_COLUMN;
        public KEY_COLOR[][] KeyColor;
    }


    // === Enums ===

    enum EFF_INDEX {
        EFF_FULL_ON(0), EFF_BREATH(1), EFF_BREATH_CYCLE(2 ), EFF_SINGLE(3),  EFF_WAVE(4),
        EFF_RIPPLE(5), EFF_CROSS(6), EFF_RAIN(7), EFF_STAR(8), EFF_SNAKE(9), EFF_REC(10),

        EFF_SPECTRUM(11), EFF_RAPID_FIRE(12), EFF_INDICATOR(13), // "mouse eff"
        // "new effect"
        EFF_FIRE_BALL(14), EFF_WATER_RIPPLE(15), EFF_REACTIVE_PUNCH(16),
        EFF_SNOWING(17), EFF_HEART_BEAT(18), EFF_REACTIVE_TORNADO(19),
        // "multi"
        EFF_MULTI_1(0xE0), EFF_MULTI_2(0xE1), EFF_MULTI_3(0xE2), EFF_MULTI_4(0xE3), EFF_OFF(0xFE);

        private final int id;
        EFF_INDEX(int id) {this.id = id;}
        public int getValue() {return id;}
    }

    enum DEVICE_INDEX {
        DEV_MKeys_L(0), DEV_MKeys_S(1), DEV_MKeys_L_White(2), DEV_MKeys_M_White(3), DEV_MMouse_L(4),
        DEV_MMouse_S(5), DEV_MKeys_M(6), DEV_MKeys_S_White(7), DEV_MM520(8), DEV_MM530(9),
        DEV_MK750(10), DEV_CK372(11), DEV_CK550_552(12), DEV_CK551(13), DEV_DEFAULT(0xFFFF);

        private final int id;
        DEVICE_INDEX(int id) {this.id = id;}
        public int getValue() {return id;}
    }

    enum LAYOUT_KEYBOARD {
        LAYOUT_UNINIT(0), LAYOUT_US(1), LAYOUT_EU(2), LAYOUT_JP(3);

        private final int id;
        LAYOUT_KEYBOARD(int id) {this.id = id;}
        public int getValue() {return id;}
    }


    // === Methods ===

    // Get current system time in Y m/d H:M:S format
    // TODO: implement properly or remove
    // String GetNowTime();

    // Return API version
    int GetCM_SDK_DllVer();


    // --- System functions ---

    // Return current CPU utilisation in the range 0-100
    // Does not appear to work
    NativeLong GetNowCPUUsage(IntByReference pErrorCode);

    // Returns RAM usage percent in range 0-100
    int GetRamUsage();

    // Returns current loudness of audio output, independent of Windows volume setting, from 0-1 range
    float GetNowVolumePeekValue();

    // --- Device functions ---
    // Configure the API to control a specific device
    // Use a device ID from DEVICE_INDEX enum
    void SetControlDevice(int devIndex);

    // Test if a particular device type is currently plugged in
    // Use ID from DEVICE_INDEX enum; default is DEV_DEFAULT
    // Default option appears to return true if any CM device is connected
    boolean IsDevicePlug(int devIndex);

    // Attempt to discover the keyboard layout
    // Only works if SetControlDevice called previously
    int GetDeviceLayout(int devIndex);

    // Enable or disable software LED control for a device
    // bEnable set true will take software control; false returns control to the peripheral
    boolean EnableLedControl(boolean bEnable, int devIndex);

    // The following functions return a boolean, set true iff the function executed correctly.
    // Take control of the keyboard before using these.

    // Set a specific effect on the peripheral; use values from the EFFECT_INDEX enum
    boolean SwitchLedEffect(int iEffectIndex, int devIndex);

    // Configure buffer flushing or force a flush
    // bAuto set true automatically flushes the buffer; false causes every such call to flush
    boolean RefreshLed(boolean bAuto, int devIndex);

    // Note that, for the following functions, a non-RGB device only interprets the red channel.
    // Anything configured for the green and blue channels is ignored.

    // Set all LEDs on the device to given values
    boolean SetFullLedColor(byte r, byte g, byte b, int devIndex);

    // Set all LEDs on the device according to a COLOR_MATRIX
    boolean SetAllLedColor(COLOR_MATRIX colorMatrix, int devIndex);

    // Set a specific LED on the device
    boolean SetLedColor(int iRow, int iColumn, byte r, byte g, byte b, int devIndex);
}

