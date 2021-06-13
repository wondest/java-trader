package com.nature.jt.data;

import com.nature.jt.data.feed.DataFeed;
import com.nature.jt.data.source.DataSource;

/**
 * @ClassName DataFactory
 * @Description: TODO
 * @Author Tender
 * @Time 2021/6/13 22:22
 * @Version 1.0
 * @Since 1.8
 **/
public interface DataFactory {
    /**
     * 创建一个数据
     * @return
     */
    DataFeed createFeed();

    /**
     * 创建一个数据源
     * @return
     */
    DataSource createSource();
}
