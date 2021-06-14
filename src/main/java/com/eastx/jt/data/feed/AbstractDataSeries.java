package com.eastx.jt.data.feed;

import com.eastx.jt.buffer.AbstractLines;
import com.eastx.jt.buffer.LineSingle;

/**
 * @ClassName AbstractDataFeed
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/26 21:34
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class AbstractDataSeries extends AbstractLines implements DataSeries {
    private static final int LINE_ORDER_DATETIME = 0;
    private static final int LINE_ORDER_CLOSE= 1;
    private static final int LINE_ORDER_LOW = 2;
    private static final int LINE_ORDER_HIGH = 3;
    private static final int LINE_ORDER_OPEN = 4;
    private static final int LINE_ORDER_VOLUME = 5;
    private static final int LINE_ORDER_INTEREST = 6;
    private static final int LINE_SIZE = 7;

    private static final String[] lineAlias = {"datetime", "close", "low", "high", "open", "volume", "interest"};

    public AbstractDataSeries() {
        super(lineAlias);
    }

    @Override
    public LineSingle close() {
        return getLine(LINE_ORDER_CLOSE);
    }

    @Override
    public LineSingle datetime() {
        return getLine(LINE_ORDER_DATETIME);
    }
}
