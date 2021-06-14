package com.eastx.jt.data.feed;

import com.eastx.jt.DateUtil;
import com.eastx.jt.buffer.BoxDouble;
import com.eastx.jt.data.source.AbstractDataSource;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @ClassName CsvDataFeed
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/30 22:44
 * @Version 1.0
 * @Since 1.8
 **/
public class CsvDataFeed extends AbstractDataSeries implements DataFeed{
    private static Logger log = Logger.getLogger(CsvDataFeed.class);

    /**
     * Table titles.
     */
    private final int headers;

    /**
     * Row fields separator.
     */
    private final String separator;

    /**
     * Date format.
     */
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * The number of row processed.
     */
    private int processed = 0;

    /**
     * The number of row processed.
     */
    private int errors = 0;

    /**
     *
     */
    private static int TOO_MANY_ERRORS = 100;

    public CsvDataFeed(int headers, String separator) {
        this.headers = headers;
        this.separator = separator;
    }

    public CsvDataFeed() {
        this(1, ",");
    }

    /**
     * Process a bar.
     * @param oneBar
     * @return
     */
    private boolean process(Object oneBar) {
        String[] fields = map(oneBar).split(separator);

        try {
            datetime().append(BoxDouble.dateOf(DateUtil.str2num(fields[0], format)));
            close().append(BoxDouble.valueOf(fields[1]));
            return true;
        } catch (ParseException e) {
            errors++;
            log.error(String.format("日期格式非法:{processed=%d}{bar=%s}", processed, oneBar), e);
        } finally {
            return false;
        }
    }

    /**
     *
     * @return
     */
    private boolean ignoreError() {
        return (errors <= TOO_MANY_ERRORS);
    }

    /**
     *
     * @return
     */
    private boolean ignoreHeader() {
        return (processed++) < headers;
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
        if(!ignoreError()) {
            log.debug(String.format("错误记录数量超过上限:errors=%d limit=%d", errors, TOO_MANY_ERRORS));
            return false;
        } else if(ignoreHeader()) {
            log.debug(String.format("跳过标题内容:processed=%d headers=%d", processed, headers));
            return false;
        } else {
            return process(oneBar);
        }
    }

    @Override
    public DataSeries getData() {
        return this;
    }
}
