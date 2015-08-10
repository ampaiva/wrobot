package com.ampaiva.wrobot.client;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.ampaiva.hlo.util.Helper;

public class JavaScriptTest {

    @Test
    public void testExecute() throws ScriptException, IOException {
        JavaScript javaScript = new JavaScript();
        String script = Helper.convertInputStream2String(JavaScriptTest.class.getResourceAsStream("/vonix-1.3.min.js"));
        javaScript.eval(script);
    }
}
