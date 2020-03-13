package com.zs.create.modules.script.service.impl;

import com.zs.create.common.groovy.GroovyBinding;
import com.zs.create.common.groovy.IGroovyScriptEngine;
import com.zs.create.common.groovy.IScript;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Auther: guodl
 * @Date: 2019/8/9 17:20
 * @Description:脚本引擎
 */
@Slf4j
@Service("groovyScriptEngine")
public class GroovyScriptEngine implements IGroovyScriptEngine, ApplicationListener<ContextRefreshedEvent> {
    private GroovyBinding groovyBinding = new GroovyBinding();

    @Override
    public void execute(String script) {
        executeObject(script, null);
    }

    @Override
    public void execute(String script, Map<String, Object> vars) {
        executeObject(script, vars);
    }

    @Override
    public boolean executeBoolean(String script, Map<String, Object> vars) {
        return (Boolean) executeObject(script, vars);
    }

    @Override
    public String executeString(String script, Map<String, Object> vars) {
        return (String) executeObject(script, vars);
    }


    @Override
    public int executeInt(String script, Map<String, Object> vars) {
        return (Integer) executeObject(script, vars);
    }

    @Override
    public float executeFloat(String script, Map<String, Object> vars) {
        return (Float) executeObject(script, vars);
    }

    @Override
    public Object executeObject(String script, Map<String, Object> vars) {
        groovyBinding.setThreadVariables(vars);

        if (log.isDebugEnabled()) {
            log.debug("执行:" + script);
            log.debug("variables:" + vars + "");
        }

        GroovyShell shell = new GroovyShell(groovyBinding);
        script = script.replace("&apos;", "'").replace("&quot;", "\"")
                .replace("&gt;", ">").replace("&lt;", "<")
                .replace("&nuot;", "\n").replace("&amp;", "&");

        Object rtn = shell.evaluate(script);
        return rtn;
    }

    //所有的spring来管理的脚本 必须要实现IScript
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Map<String, IScript> scirptImpls = event.getApplicationContext().getBeansOfType(IScript.class);
            for (Map.Entry<String, IScript> scriptMap : scirptImpls.entrySet()) {
                groovyBinding.setProperty(scriptMap.getKey(), scriptMap.getValue());
            }
        }
    }
}
