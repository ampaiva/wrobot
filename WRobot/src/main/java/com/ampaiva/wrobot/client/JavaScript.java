package com.ampaiva.wrobot.client;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.NativeObject;

public class JavaScript {

    private ScriptEngineManager mgr = new ScriptEngineManager();
    private ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");

    public void eval(String script) throws ScriptException {
        jsEngine.eval(script);
    }

    public Object get(String variable) {
        Object nativeObject = jsEngine.get(variable);
        return nativeObject;
    }

    void execute() throws ScriptException {

        String buildInfoJS = "var build_info = new Object;build_info.BUILD_DISPLAY_VERSION       = 'Build 201302232300'";
        eval(buildInfoJS);
        Object nativeObject = get("build_info");

        if (nativeObject instanceof NativeObject) {
            NativeObject nObj = (NativeObject) nativeObject;
            for (Object key : nObj.getAllIds()) {
                System.out.println(key);
                System.out.println(nObj.get((String) key, nObj));
            }
        }
    }
}
