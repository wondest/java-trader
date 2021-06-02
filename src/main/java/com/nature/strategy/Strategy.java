package com.nature.strategy;

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
     * Run the strategy.
     */
    public void evalOnce();

    /**
     * Run the strategy with the bar.
     */
    public void evalNext();
}
