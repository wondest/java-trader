package com.nature.jt.indicator.base;

import com.nature.jt.buffer.BoxDouble;
import com.nature.jt.buffer.LineSingle;

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
        protected void nextFirst() {
            doEvalNext();
        }

        @Override
        protected void nextRemaining() {
            doEvalNext();
        }

        @Override
        protected void doEvalNext() {
            doEvalOnce(barIndex());
        }

        @Override
        protected void doEvalOnce(int i) {
            set(i, data0.slice(i-period()+1, i+1)
                      .reduce(BoxDouble.ZERO, BoxDouble::add)
                      .div(BoxDouble.valueOf(period())));
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
        protected void onceFirst(int startInclusive, int endExclusive) {
            this.set(startInclusive, data0.get(startInclusive).sub(data1.get(startInclusive)));
        }

        @Override
        protected void nextFirst() {
            setBar(data0.getBar().sub(data1.getBar()));
        }

        @Override
        protected void nextRemaining() {
            doEvalNext();
        }

        @Override
        protected void doEvalNext() {
            doEvalOnce(barIndex());
        }

        @Override
        protected void doEvalOnce(int i) {
            BoxDouble prev = get(i-1);
            BoxDouble diff = data0.get(i).sub(data1.get(i));
            set(i, diff.compareTo(BoxDouble.ZERO)==0?prev:diff);
        }
    }
}
