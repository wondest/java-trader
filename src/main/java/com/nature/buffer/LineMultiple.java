package com.nature.buffer;

/**
 * @author Tender
 */
public interface LineMultiple extends LineSeries {
    /**
     *  获取Multiple中的LineBuffer
     * @param order
     * @return
     */
    public LineBuffer getLine(int order);
}
