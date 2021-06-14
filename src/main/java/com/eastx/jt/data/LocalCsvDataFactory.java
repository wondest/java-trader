package com.eastx.jt.data;

import com.eastx.jt.data.feed.CsvDataFeed;
import com.eastx.jt.data.feed.DataFeed;
import com.eastx.jt.data.source.DataSource;
import com.eastx.jt.data.source.FlatFileDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName LocalCsvDataFactory
 * @Description: 一个本地文件为数据源的工厂
 * @Author Tender
 * @Time 2021/6/13 22:33
 * @Version 1.0
 * @Since 1.8
 **/
public class LocalCsvDataFactory implements DataFactory {
    /**
     * 源文件全路径
     */
    private String fileName;

    public LocalCsvDataFactory(String fileName) {
        this.fileName = checkNotNull(fileName, "fileName should not be null");
    }

    @Override
    public DataFeed createFeed() {
        return new CsvDataFeed();
    }

    @Override
    public DataSource createSource() {
        return new FlatFileDataSource(fileName);
    }
}
