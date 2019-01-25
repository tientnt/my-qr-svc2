package com.am.common.utils;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import play.Environment;

public class Module extends AbstractModule {

    private final Environment environment;

    private final Config config;

    public Module(Environment environment, Config config) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    protected void configure() {
        Singleton.config = this.config;
    }
}
