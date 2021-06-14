package com.eastx.jt.strategy;

import com.eastx.jt.data.feed.DataSeries;

/**
 * @ClassName Strategies
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 23:18
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class Strategies {
    /**
     * The meta class for a strategy.
     */
    public static abstract class ClsStrategy {
        /**
         * create a strategy.
         * @param data
         * @return
         */
        abstract public Strategy make(DataSeries...data);

        /**
         * The min number of the necessary datas for a strategy.
         * @return
         */
        abstract public int minData();
    }
}
