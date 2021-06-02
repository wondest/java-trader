package com.nature.indicator.base;

import com.nature.buffer.BufferUtil;
import com.nature.buffer.LineSingle;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * @ClassName BasicOps
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:20
 * @Version 1.0
 * @Since 1.8
 **/
public class BasicOps {

    public static class Average extends Indicators.Eval {
        //Input: data
        LineSingle data0;

        public Average(int period, LineSingle data0) {
            super("Average_" + period, period, data0);
            this.data0 = data0;
        }

        @Override
        protected void nextStart() {
            doEvalNext();
        }

        @Override
        protected void nextRemaining() {
            doEvalNext();
        }

        @Override
        protected void doEvalNext() {
            doEvalOnce(barIdx());
        }

        @Override
        protected void doEvalOnce(int i) {
            set(i, data0.slice(i-period()+1, i+1)
                      .reduce(BigDecimal.ZERO, BigDecimal::add)
                      .divide(BufferUtil.valueOf(period()), BigDecimal.ROUND_HALF_UP));
        }
    }

    public static class NonZeroDifference extends Indicators.Eval {
        //Input: data
        LineSingle data0;

        //Input: data
        LineSingle data1;

        public NonZeroDifference(int period, LineSingle data0, LineSingle data1) {
            super("nzd", period, data0);
            this.data0 = data0;
            this.data1 = data1;
        }

        @Override
        protected void onceStart(int startInclusive, int endExclusive) {
            this.set(startInclusive, data0.get(startInclusive).subtract(data1.get(startInclusive)));
        }

        @Override
        protected void nextStart() {
            setBar(data0.getBar().subtract(data1.getBar()));
        }

        @Override
        protected void nextRemaining() {
            doEvalNext();
        }

        @Override
        protected void doEvalNext() {
            doEvalOnce(barIdx());
        }

        @Override
        protected void doEvalOnce(int i) {
            BigDecimal prev = get(i-1);
            BigDecimal diff = data0.get(i).subtract(data1.get(i));
            set(i, diff.compareTo(BigDecimal.ZERO)==0?prev:diff);
        }
    }
}
