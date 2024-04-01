package org.example.jloxrunner;

import org.springframework.context.annotation.Configuration;

public class JloxRunnerConfig {

    public JLoxRunner makeJloxRunner() {
        return new JLoxRepelRunner();
    }

}
