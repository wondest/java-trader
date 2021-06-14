package com.eastx.jt.indicator.core;


import com.eastx.jt.indicator.base.AbstractIndicators;
import com.eastx.jt.buffer.LineSingle;
import com.eastx.jt.indicator.base.BasicIndicators;

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
