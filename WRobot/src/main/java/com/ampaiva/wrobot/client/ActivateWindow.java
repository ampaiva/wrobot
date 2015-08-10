package com.ampaiva.wrobot.client;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;
 
public class ActivateWindow {
 
    public interface User32 extends W32APIOptions {
 
          User32 instance = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);
           
         // interface WNDENUMPROC extends StdCallCallback {
         //      boolean callback(Pointer hWnd, Pointer arg);
         //  }
 
         // boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
             
          boolean ShowWindow(HWND hWnd, int nCmdShow);
          boolean SetForegroundWindow(HWND hWnd);
          HWND FindWindow(String winClass, String title);
           
          int SW_SHOW = 1;
           
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        User32 user32 = User32.instance;
        HWND hWnd = user32.FindWindow(null, "any application which is hide in the system tray, and put its title here");
        user32.ShowWindow(hWnd, User32.SW_SHOW);
        user32.SetForegroundWindow(hWnd);
    }
 
}