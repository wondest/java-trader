package com.nature.indicator;

import com.nature.buffer.LineSingle;
import com.nature.indicator.base.BasicOps;
import com.nature.indicator.base.Indicators;

import java.math.BigDecimal;

/**
 * @ClassName CrossOver
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:38
 * @Version 1.0
 * @Since 1.8
 **/
public class CrossOver extends Indicators.Eval implements Indicator {
    LineSingle data0;
    LineSingle data1;

    public CrossOver(Indicator ind0, Indicator ind1) {
        super("CrossOver", Math.max(ind0.period(), ind1.period()), ind0);

        data0 = addIndicator(new CrossUp(ind0, ind1));
        data1 = addIndicator(new CrossDown(ind0, ind1));
    }

    @Override
    protected void doEvalOnce(int i) {
        set(i, data0.get(i).subtract(data1.get(i)));
    }

    @Override
    protected void doEvalNext() {
        doEvalOnce(barIdx());
    }

    @Override
    protected void nextStart() {
        doEvalNext();
    }

    @Override
    protected void nextRemaining() {
        doEvalNext();
    }

    private static class CrossBase extends Indicators.Eval {
        private Indicator nzd;
        private LineSingle data0;
        private LineSingle data1;

        private boolean isCrossUp;

        CrossBase(int period, LineSingle data0, LineSingle data1, boolean isCrossUp) {
            super("CrossBase", period, data0);

            this.nzd = addIndicator(new BasicOps.NonZeroDifference(period, data0, data1));
            this.data0 = data0;
            this.data1 = data1;
            this.isCrossUp = isCrossUp;
        }

        @Override
        protected void onceStart(int startInclusive, int endExclusive) {
            set(startInclusive, BigDecimal.ZERO);
        }

        @Override
        protected void doEvalOnce(int i) {
            boolean isBelow = false;
            boolean isAbove = false;

            //-1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
            int beforeCompared = nzd.get(i-1).compareTo(BigDecimal.ZERO);
            int afterCompared = data0.get(i).subtract(data1.get(i)).compareTo(BigDecimal.ZERO);

            if(isCrossUp) {
                isBelow = (beforeCompared == -1);
                isAbove = (afterCompared == 1);
            } else {
                isAbove = (beforeCompared == 1);
                isBelow = (afterCompared == -1);
            }

            set(i, isBelow & isBelow?1.0:0.0);
        }

        @Override
        protected void nextStart() {
            setBar(BigDecimal.ZERO);
        }

        @Override
        protected void nextRemaining() {
            doEvalNext();
        }

        @Override
        protected void doEvalNext() {
            doEvalOnce(barIdx());
        }
    }

    private static class CrossUp extends Indicators.Proxy {
        CrossUp(int period, LineSingle data0, LineSingle data1) {
            super("CrossUp", period, new CrossBase(period, data0, data1, true));
        }

        CrossUp(Indicator ind0, Indicator ind1) {
            this(Math.max(ind0.period(), ind1.period()), ind0, ind1);
        }
    }

    private static class CrossDown extends Indicators.Proxy {
        CrossDown(int period, LineSingle data0, LineSingle data1) {
            super("CrossDown", period, new CrossBase(period, data0, data1, false));
        }

        CrossDown(Indicator ind0, Indicator ind1) {
            this(Math.max(ind0.period(), ind1.period()), ind0, ind1);
        }
    }
}
