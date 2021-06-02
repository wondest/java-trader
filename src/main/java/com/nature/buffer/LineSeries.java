package com.nature.buffer;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 *
 * 基于时间序列的Bar的接口实现
 *
 * @author Tender
 */
public interface LineSeries {
    /**
     * 返回Buffer中的bar的长度
     *
     * @return
     */
    public int barLen();


    /**
     * 返回Buffer中的数据长度
     * @return
     */
    public int bufLen();

    /**
     * 返回Buffer中的bar的当前指针
     * @return
     */
    public int barIdx();

    /**
     * 重置Bar指针
     */
    public void reset();

    /**
     * 回归Bar指针,到起始位置,这个位置是个初始位置
     */
    public void home();

    /**
     * 推进1个Bar元素
     */
    public void forward();

    /**
     * 推进n个Bar元素
     * @param n
     */
    public void forward(int n);

    /**
     * 回退1个Bar元素
     */
    public void backwards();

    /**
     * 回退n个Bar元素
     * @param size
     */
    public void backwards(int size);

    /**
     * 推进1个Bar指针
     */
    public void advance();

    /**
     * 推进n个Bar指针
     * @param size
     */
    public void advance(int size);

    /**
     * 回退1个Bar指针
     */
    public void rewind();

    /**
     * 回退n个Bar指针
     * @param size
     */
    public void rewind(int size);

    /**
     * 设置当前Bar指针偏移offset的Bar元素
     * @param offset
     * @param item
     */
    public void setBar(int offset, BigDecimal item);

    /**
     * 设置当前Bar指针的Bar元素
     * @param item
     */
    public void setBar(BigDecimal item);

    /**
     * 获取当前Bar指针偏移offset的Bar元素
     * @param offset
     * @return
     */
    public BigDecimal getBar(int offset);

    /**
     * 获取当前Bar指针的Bar元素
     * @return
     */
    public BigDecimal getBar();

    /**
     * 获取指定当前Bar指定区间的所有Bar元素
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public Stream<BigDecimal> between(int startInclusive, int endExclusive);
}
