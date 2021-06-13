package com.nature.data.feed;


/**
 * @ClassName DataFeed
 * @Description: Consume one bar,feed to DataSeries.
 * @Author Tender
 * @Time 2021/5/30 22:44
 * @Version 1.0
 * @Since 1.8
 **/
public interface DataFeed<T> {
    /**
     * Get data
     * @return
     */
    public DataSeries getData();

    /**
     * Feed data
     * @param oneBar
     * @return
     */
    public boolean accept(T oneBar);
}
