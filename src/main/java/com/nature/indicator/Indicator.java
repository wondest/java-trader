package com.nature.indicator;

import com.nature.jt.buffer.LineSingle;

/**
 * @ClassName Indicator
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 15:57
 * @Version 1.0
 * @Since 1.8
 **/
public interface Indicator extends LineSingle {
    /**
     *
     * @return
     */
    public int period();

    /**
     *
     */
    public void evalOnce();

    /**
     *
     */
    public void evalNext();

    /**
     *
     * @return
     */
    public LineSingle getLine();
}
