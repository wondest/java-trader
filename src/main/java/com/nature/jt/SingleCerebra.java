package com.nature.jt;

import com.nature.data.DataFactory;
import com.nature.data.feed.DataFeed;
import com.nature.data.source.DataSource;
import com.google.common.eventbus.Subscribe;
import com.nature.jt.strategy.Strategies;
import com.nature.jt.strategy.Strategy;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName StrategyEngine
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
    private DataSource<String> master;

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

    private void check() {
        checkNotNull(master, "master shoud not be null, please check");
        checkNotNull(feeder, "feeder shoud not be null, please check");
        checkNotNull(runStrats, "runStrats shoud not be null, please check");
    }

    public void run() {
        check();

        if(isFlow()) {
            this.master.registry(this);

            if(master.canAll()) {
                master.postAll();
            } else {
                log.debug("Waiting for master's next bar...");
            }
        } else {
            master.forAll(feeder::accept);
            evalOnce();
        }
    }

    private void evalOnce() {
        runStrats.stream().parallel().forEach(s->s.evalOnce());
    }

    private void evalFlow(String oneBar) {
        if(feeder.accept(oneBar)) {
            feeder.getData().advance();
            runStrats.stream().parallel().forEach(s -> s.evalNext());
        }
    }

    @Subscribe
    private void receive(String oneBar) {
        evalFlow(oneBar);
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

    public SingleCerebra addStrategy(Strategies.ClsStrategy clsStrategy) {
        this.clsStrats.add(clsStrategy);
        return this;
    }

    public SingleCerebra flow() {
        this.flow = true;
        return this;
    }

    public SingleCerebra build() {
        //create data
        this.master = factory.createSource();
        this.feeder = factory.createFeed();

        //initialize the strategies
        runStrats = clsStrats.stream().map(cls->cls.make(feeder.getData())).collect(Collectors.toList());

        return this;
    }
}