package com.nature.buffer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @ClassName AbstractList
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 15:41
 * @Version 1.0
 * @Since 1.8
 **/
public class ArrayVector implements LineVector<BigDecimal> {
    private ArrayList<BigDecimal> array = new ArrayList<BigDecimal>();

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public BigDecimal set(int index, BigDecimal element) {
        return array.set(index, element);
    }

    @Override
    public BigDecimal set(int index, double element) {
        return set(index, BigDecimal.valueOf(element));
    }

    @Override
    public BigDecimal get(int index) {
        return array.get(index);
    }

    @Override
    public void clear() {
        array.clear();
    }

    @Override
    public boolean append(BigDecimal element) {
        return array.add(element);
    }

    @Override
    public boolean addAll(Collection<BigDecimal> c) {
        return array.addAll(c);
    }

    @Override
    public Stream<BigDecimal> slice(int startInclusive, int endExclusive) {
        return array.subList(startInclusive, endExclusive).stream();
    }
}