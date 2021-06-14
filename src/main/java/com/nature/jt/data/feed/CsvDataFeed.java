package com.nature.jt.data.feed;

import com.nature.jt.buffer.BoxDouble;

/**
 * @ClassName CsvDataFeed
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/30 22:44
 * @Version 1.0
 * @Since 1.8
 **/
public class CsvDataFeed extends AbstractDataSeries implements DataFeed{
    /**
     * Table titles.
     */
    private final int headers;

    /**
     * Row fields separator.
     */
    private final String separator;

    /**
     * The number of row processed.
     */
    private int processed = 0;

    public CsvDataFeed(int headers, String separator) {
        this.headers = headers;
        this.separator = separator;
    }

    public CsvDataFeed() {
        this(1, ",");
    }

    private boolean ignoreHeader() {
        return (processed++) < headers;
    }

    /**
     * Process a bar.
     * @param oneBar
     * @return
     */
    private boolean process(Object oneBar) {
        String[] fields = map(oneBar).split(separator);

        close().append(BoxDouble.valueOf(fields[1]));

        return true;
    }

    /**
     * Map the raw to inner data structure.
     * @param raw
     * @return
     */
    private String map(Object raw) {
        return (String)raw;
    }

    /**
     * Consume a bar.
     * @param oneBar
     * @return
     */
    @Override
    public boolean accept(Object oneBar) {
        if(!ignoreHeader()) {
            return process(oneBar);
        } else {
            return false;
        }
    }

    @Override
    public DataSeries getData() {
        return this;
    }
}
