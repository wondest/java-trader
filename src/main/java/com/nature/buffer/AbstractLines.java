package com.nature.buffer;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName LineData
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 15:43
 * @Version 1.0
 * @Since 1.8
 **/
public abstract class AbstractLines implements LineMultiple {
    private LineBuffer[] lines;
    private int lineSize;

    private LineBuffer lineMaster;

    public AbstractLines(int lineSize) {
        this.lines = new LineBuffer[lineSize];
        IntStream.range(0, lineSize).forEach(i -> this.lines[i] = new LineBuffer());
        this.lineMaster = lines[0];
        this.lineSize = lineSize;
    }

    public AbstractLines(String[] lineAlias) {
        checkNotNull(lineAlias, "lineAlias must not be null");
        checkArgument(lineAlias.length > 0, "lineAlias must not be empty");

        this.lineSize = lineAlias.length;
        this.lines = new LineBuffer[lineSize];
        IntStream.range(0, lineSize).forEach(i -> this.lines[i] = new LineBuffer(lineAlias[i]));
        this.lineMaster = lines[0];
    }

    @Override
    public void reset() {
        IntStream.range(0, lineSize).forEach(i->lines[i].reset());
    }

    @Override
    public void home() {
        IntStream.range(0, lineSize).forEach(i->lines[i].home());
    }

    @Override
    public void forward() {
        IntStream.range(0, lineSize).forEach(i->lines[i].forward());
    }

    @Override
    public void forward(int size) {
        IntStream.range(0, lineSize).forEach(i->lines[i].forward(size));
    }

    @Override
    public void backwards() {
        IntStream.range(0, lineSize).forEach(i->lines[i].backwards());
    }

    @Override
    public void backwards(int size) {
        IntStream.range(0, lineSize).forEach(i->lines[i].backwards(size));
    }

    @Override
    public void setBar(int offset, BigDecimal item) {
        IntStream.range(0, lineSize).forEach(i->lines[i].setBar(offset, item));
    }

    @Override
    public void setBar(BigDecimal item) {
        IntStream.range(0, lineSize).forEach(i->lines[i].setBar(item));
    }

    @Override
    public BigDecimal getBar(int offset) {
        return lines[0].getBar(offset);
    }

    @Override
    public Stream<BigDecimal> between(int startInclusive, int endExclusive) {
        return lineMaster.slice(startInclusive, endExclusive);
    }

    @Override
    public LineBuffer getLine(int order) {
        return lines[order];
    }

    @Override
    public void advance() {
        IntStream.range(0, lineSize).forEach(i->lines[i].advance());
    }

    @Override
    public void advance(int size) {
        IntStream.range(0, lineSize).forEach(i->lines[i].advance(size));
    }

    @Override
    public void rewind() {
        IntStream.range(0, lineSize).forEach(i->lines[i].rewind());
    }

    @Override
    public void rewind(int size) {
        IntStream.range(0, lineSize).forEach(i->lines[i].rewind(size));
    }

    @Override
    public BigDecimal getBar() {
        return lineMaster.getBar();
    }

    @Override
    public int barLen() {
        return lineMaster.barLen();
    }

    @Override
    public int bufLen() {
        return lineMaster.bufLen();
    }

    @Override
    public int barIdx() {
        return lineMaster.barIdx();
    }
}
