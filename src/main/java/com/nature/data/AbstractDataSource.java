package com.nature.data;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import java.util.function.Consumer;

/**
 * @ClassName DataSource
 * @Description: 定义数据源的行为
 * @Author Tender
 * @Time 2021/5/29 21:33
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class AbstractDataSource implements DataSource {
    private static Logger log = Logger.getLogger(AbstractDataSource.class);

    /**
     * 向量型的数据源,不允许发布数据
     *
     * @functionable forAll
     */
    public abstract static class Vector extends AbstractDataSource {
        @Override
        public void postAll() {
            throw new UnsupportedOperationException("Vector's postAll is not supported");
        }

        @Override
        public void post(String oneBar) {
            throw new UnsupportedOperationException("Vector's post is not supported");
        }

        @Override
        public void registry(Object subscriber) {
            throw new UnsupportedOperationException("Vector's registry is not supported");
        }

        @Override
        public boolean isPositive() {
            return true;
        }
    }

    /**
     * 发布型的数据源,允许注册订阅,主动发布数据
     *
     * @functionable post,registry
     */
    private abstract static class Publisher extends AbstractDataSource {
        private String name;
        private EventBus eventBus;

        Publisher(String name) {
            this.eventBus = new EventBus(name);
            this.name = name;
        }

        @Override
        public void post(String oneBar) {
            log.debug(new StringBuilder(name).append("=send=>").append(oneBar).toString());
            eventBus.post(oneBar);
        }

        @Override
        public void registry(Object subscriber) {
            eventBus.register(subscriber);
        }
    }

    /**
     * 事件型的数据源,允许注册订阅,不允许主动发布数据
     *
     * @functionable post,registry
     */
    public abstract static class Event extends Publisher {
        Event(String name) {
            super(name);
        }

        @Override
        public void forAll(Consumer<String> feed) {
            throw new UnsupportedOperationException("Event's getAll is not supported");
        }

        @Override
        public void postAll() {
            throw new UnsupportedOperationException("Event's postAll is not supported");
        }

        @Override
        public boolean isPositive() {
            return false;
        }
    }

    /**
     * 代理型的数据源,代理一个数据源,让它可以发布数据
     *
     * @functionable post,registry
     * @functionable the original dataSource's function
     */
    public static class ProxyEvent extends Publisher {
        private DataSource dataSource;

        ProxyEvent(String name, DataSource dataSource) {
            super(name);
            this.dataSource = dataSource;
        }

        @Override
        public void forAll(Consumer<String> feed) {
            dataSource.forAll(feed);
        }

        @Override
        public void postAll() {
            if(dataSource.isPositive()) {
                forAll(this::post);
            } else {
                dataSource.postAll();
            }
        }

        @Override
        public boolean isPositive() {
            return dataSource.isPositive();
        }

        @Override
        public void start() {
            dataSource.start();
        }

        @Override
        public void stop() {
            dataSource.stop();
        }
    }

    /**
     * 链式数据源,允许注册订阅,并订阅其他数据源,代理父数据源的基本操作
     *
     * @functionable post,registry,postAll
     * @Subscribe
     */
    public static class ChainedEvent extends Publisher {
        private DataSource upsource;

        ChainedEvent(String name, DataSource upsource) {
            super(name);
            this.upsource = upsource;
        }

        @Subscribe
        protected void receive(String oneBar) {
            post(oneBar);
        }

        @Override
        public void forAll(Consumer<String> feed) {
            throw new UnsupportedOperationException("ChainedEvent's forEach is not supported");
        }

        @Override
        public void postAll() {
            if(isPositive()) {
                upsource.forAll(this::post);
            } else {
                upsource.postAll();
            }
        }

        @Override
        public void start() {
            upsource.start();
        }

        @Override
        public void stop() {
            upsource.stop();
        }

        @Override
        public boolean isPositive() {
            return false;
        }
    }
}
