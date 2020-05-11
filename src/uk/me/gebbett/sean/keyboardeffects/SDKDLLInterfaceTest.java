package uk.me.gebbett.sean.keyboardeffects;

import com.sun.jna.ptr.IntByReference;


public class SDKDLLInterfaceTest {
    public static void main(String[] args) {
        // TODO: load the SDK properly!
        System.setProperty("jna.library.path",
                "C:\\Users\\sean\\Downloads\\Keyboard\\SDK\\x64");

        SDKDLLInterface kbi = SDKDLLInterface.INSTANCE;

        // TODO: configure parameters nicely
        int devIndex = SDKDLLInterface.DEVICE_INDEX.DEV_MKeys_M_White.getValue();

        final byte BYTE_MIN = (byte) 0;
        final byte BYTE_MAX = (byte) 255;


        // Exercise the API

        System.out.printf("Version: %d\n", kbi.GetCM_SDK_DllVer());

        // System.out.printf("Time: %s\n", kbi.GetNowTime());

        IntByReference iref = new IntByReference();
        System.out.printf("CPU usage: %d, pErrorCode: %d\n", kbi.GetNowCPUUsage(iref).longValue(), iref.getValue());

        System.out.printf("RAM usage: %d\n", kbi.GetRamUsage());

        System.out.printf("Volume: %f\n", kbi.GetNowVolumePeekValue());

        System.out.printf("Is plug: %b\n", kbi.IsDevicePlug(
                SDKDLLInterface.DEVICE_INDEX.DEV_DEFAULT.getValue())
        );



        if (true) {
            // Exercise the keyboard communication
            System.out.println("Taking control of keyboard...");

            kbi.SetControlDevice(devIndex);
            System.out.printf("Current layout: %s\n",
                    kbi.GetDeviceLayout(SDKDLLInterface.DEVICE_INDEX.DEV_DEFAULT.getValue())
            );

            // Set the escape key to full-on
            kbi.SetLedColor(0, 0, BYTE_MAX, BYTE_MIN, BYTE_MIN, devIndex);


            System.out.println("Releasing control of keyboard...");

            kbi.EnableLedControl(false, devIndex);

            System.out.println("Done!");
        }

        // Exit here; explicit exit causes JVM crash :thonk:
    }

    private static void optionalSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
