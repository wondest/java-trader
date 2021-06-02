package com.nature;

import com.nature.data.DataSource;
import com.nature.data.DataMaster;
import com.nature.feed.CsvDataFeed;
import com.google.common.eventbus.Subscribe;
import com.nature.feed.DataFeed;
import com.nature.feed.DataSeries;
import com.nature.strategy.Strategies;
import com.nature.strategy.Strategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @ClassName StrategyEngine
 * @Description: 单数据源的策略计算
 * @Author Tender
 * @Time 2021/5/30 18:43
 * @Version 1.0
 * @Since 1.8
 **/
public class SingleCerebra {
    private static Logger log = Logger.getLogger(SingleCerebra.class);

    /**
     * Get data from dataMaster
     */
    private DataSource dataMaster;

    /**
     * flow or once
     */
    private boolean flow = false;

    /**
     * Feed data from dataMaster to dataSeries
     */
    private final DataFeed feed = new CsvDataFeed();

    /**
     * The strategy classes.
     */
    private final List<Strategies.ClsStrategy> clsStrats = new ArrayList<Strategies.ClsStrategy>();

    /**
     * The strategy instances.
     */
    private List<Strategy> runStrats;

    public SingleCerebra() {

    }

    public void attachData(DataSource dataSource) {
        this.dataMaster = dataSource;
    }

    public void addStrategy(Strategies.ClsStrategy clsStrategy) {
        this.clsStrats.add(clsStrategy);
    }

    public SingleCerebra flow() {
        this.flow = true;
        return this;
    }

    public SingleCerebra once() {
        this.flow = false;
        return this;
    }

    public boolean isFlow() {
        return flow;
    }

    public void run() {
        //Prepare Strategies
        runStrats = clsStrats.stream().map(cls->cls.make(feed.getData())).collect(Collectors.toList());

        //Prepare data
        if(dataMaster.isPositive()) {
            if(isFlow()) {
                dataMaster.registry(this);
                dataMaster.postAll();
            } else {
                dataMaster.forAll(this::feedData);
                evalOnce();
            }
        } else {
            checkArgument(isFlow(), "None-flow engine must need positive dataSource.");
            dataMaster.registry(this);
            log.debug("Waiting for dataMaster's bar");
        }
    }

    private void evalOnce() {
        runStrats.stream().parallel().forEach(s->s.evalOnce());
    }

    @Subscribe
    private void evalFlow(String oneBar) {
        if(feed.accept(oneBar)) {
            feed.getData().advance();
            runStrats.stream().parallel().forEach(s -> s.evalNext());
        }
    }

    private void feedData(String oneBar) {
        log.info(oneBar);
        feed.accept(oneBar);
    }
}
