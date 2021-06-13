package com.nature.jt.strategy;

/**
 * @ClassName Strategy
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/31 22:12
 * @Version 1.0
 * @Since 1.8
 **/
public interface Strategy {
    /**
     * Run the strategy using all bars once.
     */
    public void evalOnce();

    /**
     * Run the strategy using a bar one by one.
     */
    public void evalNext();
}
