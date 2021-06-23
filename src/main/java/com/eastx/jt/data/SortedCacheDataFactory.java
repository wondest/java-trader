package com.eastx.jt.data;

import com.eastx.jt.data.feed.CsvDataFeed;
import com.eastx.jt.data.feed.DataFeed;
import com.eastx.jt.data.source.DataSource;
import com.eastx.jt.data.source.PreCachedDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName SortedCacheDataFactory
 * @Description: 一个本地文件为数据源的工厂
 * @Author Tender
 * @Time 2021/6/13 22:33
 * @Version 1.0
 * @Since 1.8
 **/
public class SortedCacheDataFactory implements DataFactory {
    /**
     * 源文件全路径
     */
    private String fileName;

    public SortedCacheDataFactory(String fileName) {
        this.fileName = checkNotNull(fileName, "fileName should not be null");
    }

    @Override
    public DataFeed createFeed() {
        return new CsvDataFeed(0);
    }

    @Override
    public DataSource createSource() {
        return new PreCachedDataSource(fileName, 1);
    }
}
