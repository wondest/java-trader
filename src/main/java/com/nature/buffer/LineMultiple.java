package com.nature.buffer;

/**
 * 多线的基础接口
 * @author Tender
 */
public interface LineMultiple extends LineSeries<BoxDouble> {
    /**
     *  获取Multiple中的LineBuffer
     *
     * @param order
     * @return
     */
    public LineSingle getLine(int order);
}
