package com.ampaiva.wrobot.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class JNA {

    public static void main(String[] args) {
        setForegroundWindow("Sem título");
    }

    public static void setForegroundWindow(String title) {
        Pointer foundWindowPointer = new Memory(Pointer.SIZE);
        JNA.isWindowOpen(title, foundWindowPointer);
        if (foundWindowPointer.getPointer(0) != null) {
            HWND foundWindow = new HWND(foundWindowPointer.getPointer(0));
            JNA.activateWindow(foundWindow);
        }
    }

    public static void activateWindow(HWND foundWindow) {
        if (foundWindow != null) {
            User32.INSTANCE.ShowWindow(foundWindow, 9);
            User32.INSTANCE.SetForegroundWindow(foundWindow);
        }
    }

    public static void isWindowOpen(final String title, Pointer foundWindowPointer) {
        com.sun.jna.platform.win32.User32.INSTANCE.EnumWindows(new WNDENUMPROC() {

            @Override
            public boolean callback(HWND hWnd, Pointer foundWindowPointer) {
                if (foundWindowPointer != null) {
                    char[] windowText = new char[512];
                    com.sun.jna.platform.win32.User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                    String wText = Native.toString(windowText);
                    if (wText.startsWith(title)) {
                        foundWindowPointer.setPointer(0, hWnd.getPointer());
                    }
                }
                return true;

            }
        }, foundWindowPointer);
    }
}