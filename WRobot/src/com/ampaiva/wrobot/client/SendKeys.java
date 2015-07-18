package com.ampaiva.wrobot.client;

import java.awt.AWTException;
import java.awt.Robot;

public class SendKeys {
    public static void sendKeys(int keyInput[]) throws AWTException {
        Robot robot = new Robot();

        for (int i = 0; i < keyInput.length; i++) {
            robot.keyPress(keyInput[i]);
            robot.keyRelease(keyInput[i]);
        }
    }

}
