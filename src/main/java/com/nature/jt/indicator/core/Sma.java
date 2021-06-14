package com.nature.jt.indicator.core;


import com.nature.jt.buffer.LineSingle;
import com.nature.jt.indicator.base.BasicOps;
import com.nature.jt.indicator.base.Indicators;

/**
 * @ClassName Sma
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:11
 * @Version 1.0
 * @Since 1.8
 **/
public class Sma extends Indicators.Proxy {
    /**
     *
     */
    LineSingle data0;

    public Sma(int period, LineSingle data0) {
        super("Sma_" + period, period, new BasicOps.Average(period, data0));
        this.data0 = data0;
    }
}