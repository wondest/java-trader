package com.nature.jt.indicator.core;


import com.nature.jt.buffer.LineSingle;
import com.nature.jt.indicator.base.BasicIndicators;
import com.nature.jt.indicator.base.AbstractIndicators;

/**
 * @ClassName Sma
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:11
 * @Version 1.0
 * @Since 1.8
 **/
public class Sma extends AbstractIndicators.Proxy {
    /**
     * 输入的源数据
     */
    private final LineSingle data0;

    public Sma(int period, LineSingle data0) {
        super("Sma_" + period, period, new BasicIndicators.Average(period, data0));
        this.data0 = data0;
    }
}
