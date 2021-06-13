package com.nature.indicator.base;

import com.nature.buffer.BoxDouble;
import com.nature.buffer.LineBuffer;
import com.nature.buffer.LineSingle;
import com.nature.indicator.Indicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @ClassName IndicatorSeries
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 15:58
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class AbstractIndicator implements Indicator {
    /**
     *
     */
    private List<Indicator> indicators;

    /**
     *
     */
    private LineSingle line;

    /**
     *
     */
    private int period;

    /**
     *
     */
    private final LineSingle clock;

    /**
     *
     */
    private boolean isBinding;

    private String name;

    AbstractIndicator(String name, int period, final LineSingle clock, boolean isBinding) {
        this.name = name;
        this.period = period;
        this.clock = clock;
        this.isBinding = isBinding;
        this.indicators = new ArrayList<Indicator>();

        //如果使用Binding,那么结果由sub indicator绑定结果,否则结果由本指标进行计算得到
        if(isBinding) {
            this.line = null;
        } else {
            this.line = LineBuffer.makeData();
        }
    }

    protected Indicator addIndicator(Indicator indicator) {
        this.indicators.add(indicator);
        return indicator;
    }

    /**
     * 递归方式：广度遍历
     */
    @Override
    public void evalOnce() {
        //self binding
        onceBinding();

        //self forward
        onceForward();

        //subs evaluate
        indicators.stream().forEach(i->i.evalOnce());

        //self evaluate
        oncePre(0, period-1);
        onceStart(period-1, period);
        onceRemaining(period, clock.size());
    }

    @Override
    public void evalNext() {
        if (clock.barLen() == 1) {
            onceBinding();
        }

        //self forward
        nextForward();

        //subs evaluate
        indicators.stream().forEach(i->i.evalNext());

        //self evaluate
        if(clock.barLen() > period()) {
            nextRemaining();
        } else if (clock.barLen() == period()) {
            nextStart();
        } else {
            nextPre();
        }
    }

    /**
     * 启动前处理
     * @param startInclusive
     * @param endExclusive
     */
    protected abstract void oncePre(int startInclusive, int endExclusive);

    /**
     * 启动处理
     * @param startInclusive
     * @param endExclusive
     */
    protected abstract void onceStart(int startInclusive, int endExclusive);

    /**
     * 启动后处理
     * @param startInclusive
     * @param endExclusive
     */
    protected abstract void onceRemaining(int startInclusive, int endExclusive);


    protected abstract void nextPre();
    protected abstract void nextStart();
    protected abstract void nextRemaining();

    private void nextForward() {
        //确保比时钟走的慢
        if(!isBinding && clock.barLen() > line.barLen()) {
            line.forward();
        }
    }

    private void onceForward() {
        if(!isBinding) {
            line.forward(clock.size());
            line.home();
        }
    }

    private void onceBinding() {
        if(isBinding) {
            line = bindLine();
        }
    }

    /**
     *
     * @return
     */
    abstract protected LineSingle bindLine();

    @Override
    public LineSingle getLine() {
        return this.line;
    }

    @Override
    public int period() {
        return period;
    }

    @Override
    public int size() {
        return line.size();
    }

    @Override
    public BoxDouble set(int index, BoxDouble element) {
        return line.set(index, element);
    }

    @Override
    public BoxDouble set(int index, double element) {
        return line.set(index, element);
    }

    @Override
    public BoxDouble get(int index) {
        return line.get(index);
    }

    @Override
    public void clear() {
        line.clear();
    }

    @Override
    public boolean append(BoxDouble element) {
        return line.append(element);
    }

    @Override
    public boolean append(Collection<BoxDouble> c) {
        return line.append(c);
    }

    @Override
    public Stream<BoxDouble> slice(int startInclusive, int endExclusive) {
        return line.slice(startInclusive, endExclusive);
    }

    @Override
    public Stream<BoxDouble> between(int startInclusive, int endExclusive) {
        return line.between(startInclusive, endExclusive);
    }

    @Override
    public void reset() {
        line.reset();
    }

    @Override
    public void home() {
        line.home();
    }

    @Override
    public void forward() {
        line.forward();
    }

    @Override
    public void forward(int size) {
        line.forward(size);
    }

    @Override
    public void backwards() {
        line.backwards();
    }

    @Override
    public void backwards(int size) {
        line.backwards(size);
    }

    @Override
    public void setBar(int offset, BoxDouble item) {
        line.setBar(offset, item);
    }

    @Override
    public void setBar(BoxDouble item) {
        line.setBar(item);
    }

    @Override
    public void rewind() {
        line.rewind();
    }

    @Override
    public void rewind(int size) {
        line.rewind(size);
    }

    @Override
    public void advance() {
        line.advance();
    }

    @Override
    public void advance(int size) {
        line.advance(size);
    }

    @Override
    public BoxDouble getBar() {
        return line.getBar();
    }

    @Override
    public BoxDouble getBar(int offset) {
        return line.getBar(offset);
    }

    @Override
    public int barLen() {
        return line.barLen();
    }

    @Override
    public int bufLen() {
        return line.bufLen();
    }

    @Override
    public int barIndex() {
        return line.barIndex();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        return sb.append(name).append("_").append(line).toString();
    }
}