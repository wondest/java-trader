package com.eastx.jt.data.feed;

import com.eastx.jt.buffer.LineMultiple;
import com.eastx.jt.buffer.LineSingle;

/**
 * @ClassName DataSeries
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/26 21:34
 * @Version 1.0
 * @Since 1.8
 **/
public interface DataSeries extends LineMultiple {
    /**
     * 获取 close line
     * @return
     */
    public LineSingle close();

    /**
     * 获取 datetime line
     * @return
     */
    public LineSingle datetime();
}
