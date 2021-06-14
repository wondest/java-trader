package com.nature.jt.indicator.base;

import com.nature.jt.buffer.LineSingle;
import com.nature.jt.indicator.Indicator;

import java.util.stream.IntStream;

/**
 * @ClassName Indicators
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:12
 * @Version 1.0
 * @Since 1.8
 **/
public class Indicators {
    /**
     * 非绑定类指标
     */
    private abstract static class NoneBinding extends AbstractIndComposite {

        NoneBinding(String name, int period, LineSingle clock) {
            super(name, period, clock, false);
        }

        @Override
        public LineSingle proxyLine() {
            return null;
        }
    }

    /**
     * 绑定类指标
     */
    private abstract static class Binding extends AbstractIndComposite {
        Binding (String name, int period, LineSingle clock) {
            super(name, period, clock, true);
        }

        @Override
        protected void onceBefore(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void onceFirst(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void onceRemaining(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void nextBefore() {
            dummyOperation();
        }

        @Override
        protected void nextFirst() {
            dummyOperation();
        }

        @Override
        protected void nextRemaining() {
            dummyOperation();
        }

        private void dummyOperation() {
            //do nothing
        }
    }

    /**
     *  计算类指标
     */
    public abstract static class Eval extends NoneBinding {
        public Eval(String name, int period, LineSingle clock) {
            super(name, period, clock);
        }

        @Override
        protected void onceBefore(int startInclusive, int endExclusive) {
            //
        }

        @Override
        protected void onceFirst(int startInclusive, int endExclusive) {
            doEvalOnce(startInclusive);
        }

        @Override
        protected void onceRemaining(int startInclusive, int endExclusive) {
            IntStream.range(startInclusive, endExclusive).forEach(this::doEvalOnce);
        }

        @Override
        protected void nextBefore() {
            //
        }

        @Override
        protected void nextFirst() {

        }

        @Override
        protected void nextRemaining() {
            //
        }

        /**
         * 计算当前指标
         *
         * @param i
         */
        protected abstract void doEvalOnce(int i);

        /**
         * 计算当前指标
         *
         */
        protected abstract void doEvalNext();
    }

    /**
     * 代理型指标
     */
    public abstract static class Proxy extends Binding {
        //Binding: line
        private LineSingle binding;

        public Proxy(String name, int period, Indicator indicator) {
            super(name, period, indicator);
            this.binding = addIndicator(indicator);
        }

        @Override
        public LineSingle proxyLine() {
            return binding;
        }
    }
}
