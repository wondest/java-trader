

import com.nature.buffer.LineBuffer;
import com.nature.buffer.LineSingle;
import com.nature.indicator.CrossOver;
import com.nature.indicator.Indicator;
import com.nature.indicator.Sma;

import java.util.stream.IntStream;

/**
 * @ClassName TestIndicator
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 16:28
 * @Version 1.0
 * @Since 1.8
 **/
public class TestIndicator {
    public static void main(String[] args) {
        testCrossOver();
    }

    private static void testCrossOver() {
        int size = 50;
        LineSingle data0 = LineBuffer.makeData("Close");
        data0.forward(size);
        data0.home();

        IntStream.range(0, size).forEach(i -> data0.set(i, Math.random() * 10));

        Indicator sma_fast = new Sma(10, data0);
        Indicator sma_slow = new Sma(15, data0);

        sma_fast.evalOnce();
        sma_slow.evalOnce();

        System.out.println(data0);
        System.out.println(sma_fast);
        System.out.println(sma_slow);

        Indicator cross = new CrossOver(sma_fast, sma_slow);

        cross.evalOnce();

        System.out.println(cross);
    }
}
