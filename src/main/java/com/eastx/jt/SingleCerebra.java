package com.eastx.jt;

import com.eastx.jt.data.DataFactory;
import com.eastx.jt.data.feed.DataFeed;
import com.eastx.jt.data.source.DataSource;
import com.google.common.eventbus.Subscribe;
import com.eastx.jt.strategy.Strategies;
import com.eastx.jt.strategy.Strategy;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName SingleCerebra
 * @Description: 单数据源的策略计算引擎
 * @Author Tender
 * @Time 2021/5/30 18:43
 * @Version 1.0
 * @Since 1.8
 **/
public class SingleCerebra {
    /**
     * The logger instance.
     */
    private static Logger log = Logger.getLogger(SingleCerebra.class);

    /**
     * The main dataSource.
     */
    private DataSource master;

    /**
     * if flow is true then evaluate one by one,els evaluate all once.
     */
    private boolean flow = false;

    /**
     * Read data from master then feed to the dataSeries
     */
    private DataFeed feeder;

    /**
     * The strategy instances.
     */
    private List<Strategy> runStrategies = new ArrayList<Strategy>();

    private boolean isFlow() {
        return flow;
    }

    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public void setFeeder(DataFeed feeder) {
        this.feeder = feeder;
    }

    public void addStrategy(Strategy strategy) {
        this.runStrategies.add(strategy);
    }

    /**
     * Check the necessary conditions.
     */
    private void check() {
        checkNotNull(master, "master shoud not be null, please check");
        checkNotNull(feeder, "feeder shoud not be null, please check");
        checkNotNull(runStrategies, "runStrats shoud not be null, please check");
    }

    /**
     * Run all strategies.
     */
    public void run() {
        check();

        if(isFlow()) {
            this.master.registry(this);

            if(master.canAll()) {
                log.debug("Post all bars...");
                master.postAll();
            } else {
                log.debug("Waiting for master's next bar...");
            }
        } else {
            log.debug("For all bars now...");
            master.forAll(feeder::accept);
            evalOnce();
        }
    }

    /**
     * Evaluate all bars once.
     */
    private void evalOnce() {
        runStrategies.stream().parallel().forEach(s->s.evalOnce());
    }

    /**
     * Evaluate a bars one by one.
     */
    private void evalNext(String oneBar) {
        if(feeder.accept(oneBar)) {
            feeder.getData().advance();
            runStrategies.stream().parallel().forEach(s -> s.evalNext());
        }
    }

    @Subscribe
    private void receive(String oneBar) {
        evalNext(oneBar);
    }

    /**
     * The cerebra builder
     */
    public static class Builder {
        /**
         * The data factory, for create dataSource and dataFeed.
         */
        private DataFactory factory;

        /**
         * The strategy classes.
         */
        private final List<Strategies.ClsStrategy> clsStrategies = new ArrayList<Strategies.ClsStrategy>();

        /**
         * if flow is true then evaluate one by one,els evaluate all once.
         */
        private boolean flow;

        /**
         * Add strategies.
         * @param strategy
         * @return
         */
        public Builder addStrategy(Strategies.ClsStrategy strategy) {
            this.clsStrategies.add(strategy);
            return this;
        }

        /**
         * flow flag.
         * @return
         */
        public Builder flow() {
            this.flow = true;
            return this;
        }

        /**
         *
         * @param factory
         * @return
         */
        public Builder setFactory(DataFactory factory) {
            this.factory = checkNotNull(factory, "factory shoud not be null");
            return this;
        }

        public SingleCerebra build() {
            SingleCerebra cerebra = new SingleCerebra();

            //create dataSource
            cerebra.setMaster(factory.createSource());

            //create dataFeed
            DataFeed feeder = factory.createFeed();
            cerebra.setFeeder(feeder);

            //initialize the strategies
            clsStrategies.stream().map(cls->cls.make(feeder.getData())).forEach(obj -> cerebra.addStrategy(obj));

            //set flow flag
            cerebra.setFlow(flow);

            return cerebra;
        }
    }

    /**
     * The builder for the cerebra.
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }
}
