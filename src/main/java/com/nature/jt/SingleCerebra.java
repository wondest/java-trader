package com.nature.jt;

import com.nature.jt.data.DataFactory;
import com.nature.jt.data.feed.DataFeed;
import com.nature.jt.data.source.DataSource;
import com.google.common.eventbus.Subscribe;
import com.nature.jt.strategy.Strategies;
import com.nature.jt.strategy.Strategy;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private static Logger log = Logger.getLogger(SingleCerebra.class);

    /**
     * The data factory, for create dataSource and dataFeed.
     */
    private DataFactory factory;

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
     * The strategy classes.
     */
    private final List<Strategies.ClsStrategy> clsStrats = new ArrayList<Strategies.ClsStrategy>();

    /**
     * The strategy instances.
     */
    private List<Strategy> runStrats;

    private boolean isFlow() {
        return flow;
    }

    /**
     * Check the necessary conditions.
     */
    private void check() {
        checkNotNull(master, "master shoud not be null, please check");
        checkNotNull(feeder, "feeder shoud not be null, please check");
        checkNotNull(runStrats, "runStrats shoud not be null, please check");
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
        runStrats.stream().parallel().forEach(s->s.evalOnce());
    }

    /**
     * Evaluate a bars one by one.
     */
    private void evalNext(String oneBar) {
        if(feeder.accept(oneBar)) {
            feeder.getData().advance();
            runStrats.stream().parallel().forEach(s -> s.evalNext());
        }
    }

    @Subscribe
    private void receive(String oneBar) {
        evalNext(oneBar);
    }

    /**
     * The builder for the cerebra.
     * @return
     */
    public static SingleCerebra builder() {
        return new SingleCerebra();
    }

    public SingleCerebra setFactory(DataFactory factory) {
        this.factory = checkNotNull(factory, "factory shoud not be null");
        return this;
    }

    /**
     * Add strategies.
     * @param clsStrategy
     * @return
     */
    public SingleCerebra addStrategy(Strategies.ClsStrategy clsStrategy) {
        this.clsStrats.add(clsStrategy);
        return this;
    }

    /**
     * flow flag.
     * @return
     */
    public SingleCerebra flow() {
        this.flow = true;
        return this;
    }

    public SingleCerebra build() {
        //create dataSource
        this.master = factory.createSource();

        //create dataFeed
        this.feeder = factory.createFeed();

        //initialize the strategies
        runStrats = clsStrats.stream().map(cls->cls.make(feeder.getData())).collect(Collectors.toList());

        return this;
    }
}
