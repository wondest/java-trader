package com.nature.feed;


import com.nature.buffer.BufferUtil;

/**
 * @ClassName CsvDataFeed
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/30 22:44
 * @Version 1.0
 * @Since 1.8
 **/
public class CsvDataFeed extends AbstractDataSeries implements DataFeed {
    private final int headers;
    private final String separator;

    private int processed = 0;

    public CsvDataFeed(int headers, String separator) {
        this.headers = headers;
        this.separator = separator;
    }

    public CsvDataFeed() {
        this(1, ",");
    }

    private boolean doFilter() {
        return (processed++) < headers;
    }

    private boolean doPrepare() {
        return true;
    }

    private boolean doProcess(String line) {
        String[] fields = line.split(separator);

        close().append(BufferUtil.valueOf(fields[1]));

        return true;
    }

    private boolean doPost() {
        return true;
    }

    @Override
    public boolean accept(String oneBar) {
        if(!doFilter()) {
            doPrepare();
            doProcess(oneBar);
            doPost();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public DataSeries getData() {
        return this;
    }
}
