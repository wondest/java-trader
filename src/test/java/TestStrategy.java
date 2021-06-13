import com.nature.SingleCerebra;
import com.nature.feed.DataSeries;
import com.nature.indicator.CrossOver;
import com.nature.indicator.Indicator;
import com.nature.indicator.Sma;
import com.nature.strategy.Strategies;
import com.nature.strategy.Strategy;
import com.nature.buffer.LineSingle;
import com.nature.data.DataMaster;
import com.nature.data.DataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName TestCerebro
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/23 22:53
 * @Version 1.0
 * @Since 1.8
 **/
public class TestStrategy {

    public static class MyStrategy extends Strategies.ClsStrategy {
        private final int minData = 1;

        @Override
        public Strategy make(DataSeries... data) {
            checkNotNull(data, "Strategy's data should not be null");
            checkArgument(data.length >= minData, "This strategy needs at least %s data(s)", minData);

            class RunningStrategy implements Strategy {
                private final LineSingle data0;
                private final LineSingle data1;

                private Indicator cross;

                private List<Indicator> indicators = new ArrayList<Indicator>();

                RunningStrategy(int minData, LineSingle[] data) {
                    checkNotNull(data, "Strategy's data should not be null.");
                    checkArgument(data.length >= minData, "This strategy needs at least %s data(s).", minData);

                    this.data0 = data[0];
                    this.data1 = data[1];

                    this.cross = createIndicator();
                }

                private Indicator createIndicator() {
                    Indicator sma_fast = new Sma(10, data1);
                    Indicator sma_slow = new Sma(15, data1);
                    Indicator cross = new CrossOver(sma_fast, sma_slow);

                    indicators.add(sma_fast);
                    indicators.add(sma_slow);
                    indicators.add(cross);

                    return cross;
                }

                @Override
                public void evalOnce() {
                    indicators.stream().forEach(ind -> ind.evalOnce());
                }

                @Override
                public void evalNext() {
                    indicators.stream().forEach(ind -> ind.evalNext());
                }
            }

            return new RunningStrategy(minData
                    ,new LineSingle[] {data[0].datetime(), data[0].close()});
        }

        @Override
        public int minData() {
            return minData;
        }
    }

    public static void main(String[] args) {
        DataSource ds1 = DataMaster.makeCsvEvent("src/test/resources/trade_000001.SZ.csv");
        DataSource ds2 = DataMaster.makeChainedEvent(ds1);

        SingleCerebra cerebra = new SingleCerebra();

        cerebra.attachData(ds1);

        cerebra.addStrategy(new MyStrategy());

        cerebra.flow().run();

        System.out.println("================");
    }
}
