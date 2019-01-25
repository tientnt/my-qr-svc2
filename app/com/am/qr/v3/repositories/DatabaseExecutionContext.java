package com.am.qr.v3.repositories;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "database.dispatcher" thread pool
 */
public class DatabaseExecutionContext extends CustomExecutionContext {

    private static final String name = "database.dispatcher";

    @Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, name);
    }
}
