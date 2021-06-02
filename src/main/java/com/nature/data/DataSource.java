package com.nature.data;

import java.util.function.Consumer;

/**
 * @ClassName DataSource
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/29 21:33
 * @Version 1.0
 * @Since 1.8
 **/
public interface DataSource {
    /**
     *
     * Feed all data once when the datasource is positive.
     *
     * @condition Positive
     * @param feed
     */
    void forAll(Consumer<String> feed);

    /**
     * Start to post all data immediately.
     *
     * @condition Positive && Event
     */
    void postAll();

    /**
     * Start to post one data immediately.
     *
     * @condition Event
     * @param oneBar
     */
    void post(String oneBar);

    /**
     * Registry the subscriber.
     * within Google's EventBus using @Subscribe to marks a method as an event subscriber.
     *
     * @condition Event
     * @param subscriber
     *
     */
    void registry(Object subscriber);


    /**
     * Decide the provider is positive or not.
     *
     * @return true when the datasource is positive.
     */
    boolean isPositive();

    /**
     * Start action
     */
    void start();

    /**
     * Stop action
     */
    void stop();
}
