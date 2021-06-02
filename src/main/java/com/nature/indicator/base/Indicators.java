package com.nature.indicator.base;

import com.nature.buffer.LineSingle;
import com.nature.indicator.Indicator;

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
    private abstract static class NoneBinding extends AbstractIndicator {

        NoneBinding(String name, int period, LineSingle clock) {
            super(name, period, clock, false);
        }

        @Override
        public LineSingle bindLine() {
            return null;
        }
    }

    /**
     * 绑定类指标
     */
    abstract static class Binding extends AbstractIndicator {

        Binding (String name, int period, LineSingle clock) {
            super(name, period, clock, true);
        }

        @Override
        protected void oncePre(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void onceStart(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void onceRemaining(int startInclusive, int endExclusive) {
            dummyOperation();
        }

        @Override
        protected void nextPre() {
            dummyOperation();
        }

        @Override
        protected void nextStart() {
            dummyOperation();
        }

        @Override
        protected void nextRemaining() {
            dummyOperation();
        }

        private void dummyOperation() {
            //Do Nothing
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
        protected void oncePre(int startInclusive, int endExclusive) {
            //
        }

        @Override
        protected void onceStart(int startInclusive, int endExclusive) {
            doEvalOnce(startInclusive);
        }

        @Override
        protected void onceRemaining(int startInclusive, int endExclusive) {
            IntStream.range(startInclusive, endExclusive).forEach(this::doEvalOnce);
        }

        @Override
        protected void nextPre() {
            //
        }

        @Override
        protected void nextStart() {

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
        LineSingle bindling;

        public Proxy(String name, int period, Indicator indicator) {
            super(name, period, indicator);
            this.bindling = addIndicator(indicator);
        }

        @Override
        public LineSingle bindLine() {
            return bindling;
        }
    }
}
