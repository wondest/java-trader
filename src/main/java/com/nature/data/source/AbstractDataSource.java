package com.nature.data.source;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName DataSource
 * @Description: 定义数据源的抽象行为
 * @Author Tender
 * @Time 2021/5/29 21:33
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class AbstractDataSource<T> implements DataSource<T> {
    private static Logger log = Logger.getLogger(AbstractDataSource.class);

    /**
     * 持久型的数据源,不允许发布数据
     *
     * 例如：本地文件数据源
     *
     * @function forAll
     */
    public abstract static class Persister<T> extends AbstractDataSource<T> {
        @Override
        public void postAll() {
            throw new UnsupportedOperationException("Persister's postAll is not supported");
        }

        @Override
        public void post(T oneBar) {
            throw new UnsupportedOperationException("Persister's post is not supported");
        }

        @Override
        public void registry(Object subscriber) {
            throw new UnsupportedOperationException("Persister's registry is not supported");
        }

        @Override
        public boolean canAll() {
            return true;
        }

        @Override
        public boolean isPassive() {
            return false;
        }
    }

    /**
     * 发布型的数据源,允许注册订阅,主动发布数据
     *
     * @function post
     * @function registry
     */
    private abstract static class Publisher<T> extends AbstractDataSource<T> {
        /**
         * A brief name for this dataSource, for logging purposes.
         */
        private String name;

        /**
         * A common publisher.
         */
        private EventBus eventBus;

        Publisher(String name) {
            this.name = checkNotNull(name, "name shoud not be null");
            this.eventBus = new EventBus(name);
        }

        @Override
        public void post(T oneBar) {
            eventBus.post(oneBar);
        }

        @Override
        public void registry(Object subscriber) {
            eventBus.register(subscriber);
        }

        @Override
        public boolean isPassive() {
            return false;
        }
    }

    /**
     * 代理型的数据源,代理一个数据源,让它可以发布数据
     *
     * @function post
     * @function registry
     * @function postAll
     * @function forAll
     */
    public static class Proxy<T> extends Publisher<T> {
        /**
         * Original dataSource
         */
        private DataSource dataSource;

        public Proxy(String name, DataSource dataSource) {
            super(name);
            this.dataSource = dataSource;
        }

        @Override
        public void forAll(Consumer<T> feed) {
            dataSource.forAll(feed);
        }

        @Override
        public void postAll() {
            if(dataSource.isPassive()) {
                dataSource.postAll();
            } else {
                forAll(this::post);
            }
        }

        @Override
        public boolean canAll() {
            return dataSource.canAll();
        }

        @Override
        public boolean isPassive() {
            return true;
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

//    /**
//     * 链式数据源,允许注册订阅,并订阅其他数据源,代理父数据源的基本操作
//     *
//     * @functionable post,registry,postAll
//     * @Subscribe
//     */
//    public static class ChainedEvent<T> extends Publisher<T> {
//        /**
//         *
//         */
//        private DataSource upsource;
//
//        ChainedEvent(String name, DataSource upsource) {
//            super(name);
//            this.upsource = upsource;
//        }
//
//        @Subscribe
//        protected void receive(Object barObj) {
//            post((T)barObj);
//        }
//
//        @Override
//        public void forAll(Consumer<T> feed) {
//            throw new UnsupportedOperationException("ChainedEvent's forEach is not supported");
//        }
//
//        @Override
//        public void postAll() {
//            if(isPositive()) {
//                upsource.forAll(oneBar -> post(oneBar));
//            } else {
//                upsource.postAll();
//            }
//        }
//
//        @Override
//        public void start() {
//            upsource.start();
//        }
//
//        @Override
//        public void stop() {
//            upsource.stop();
//        }
//
//        @Override
//        public boolean isPositive() {
//            return false;
//        }
//    }
}
