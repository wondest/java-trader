package com.nature.buffer;

import java.math.BigDecimal;

/**
 * @ClassName LineData
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 15:43
 * @Version 1.0
 * @Since 1.8
 **/
public class BufferUtil {

    public static BigDecimal valueOf(String value) {
        return new BigDecimal(value);
    }

    public static BigDecimal valueOf(int value) {
        return new BigDecimal(value);
    }
}
