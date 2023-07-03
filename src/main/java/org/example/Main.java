package org.example;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.LoggerFactory;
import org.example.verticles.ExtVerticle;
import org.example.verticles.MainVerticle;
import org.example.verticles.WorkerVerticle;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        LoggerFactory.initialise();
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(1000 * 60 * 60);
        Vertx vertx = Vertx.vertx(vertxOptions);
        DeploymentOptions workerOpts = new DeploymentOptions()
                .setWorker(true)
                .setInstances(2)
                .setWorkerPoolSize(2);
        vertx.deployVerticle(WorkerVerticle.class.getName(), workerOpts);
        vertx.deployVerticle(MainVerticle.class.getName());
        vertx.deployVerticle(ExtVerticle.class.getName());
    }
}