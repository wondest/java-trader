import com.nature.data.FlatFileData;
import com.nature.data.DataSource;
import com.nature.data.DataMaster;
import com.google.common.eventbus.Subscribe;

/**
 * @ClassName TestEventCompsite
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/30 8:51
 * @Version 1.0
 * @Since 1.8
 **/
public class TestDataSource {

    public static void main(String[] args) {
        testEvent();
    }

    public static void testEvent() {
        System.out.println("====== Start testEvent ======");

        DataSource ds1 = DataMaster.makeCsvEvent("src/main/resources/trade_000001.SZ.csv");
        DataSource ds2 = DataMaster.makeChainedEvent(ds1);
        DataSource ds3 = DataMaster.makeChainedEvent(ds2);

        ds3.registry(new TestDataSource());

        ds3.postAll();

        System.out.println("====== Start testEvent ======");
    }

    public static void testVector() {
        System.out.println("====== Start testVector ======");
        DataSource ds1 = new FlatFileData("src/main/resources/trade_000001.SZ.csv");
        ds1.start();
        ds1.forAll(System.out::println);
        ds1.stop();
        System.out.println("====== Start testVector ======");
    }

    @Subscribe
    public void printBar(String oneBar) {
        System.out.println("TestDataSource:" + oneBar);
    }
}
