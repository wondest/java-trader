package com.nature.strategy;

import com.nature.buffer.LineSingle;
import com.nature.feed.DataSeries;

/**
 * @ClassName Strategies
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 23:18
 * @Version 1.0
 * @Since 1.8
 **/

public abstract class Strategies {

    public static abstract class ClsStrategy {
        /**
         * create the Strategy instance.
         * @param data
         * @return
         */
        abstract public Strategy make (DataSeries...data);

        /**
         * The min number of data for the strategy.
         * @return the min data
         */
        abstract public int minData ();
    }
}
