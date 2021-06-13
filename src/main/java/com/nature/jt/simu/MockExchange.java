package com.nature.jt.simu;

import com.google.common.eventbus.EventBus;
import com.nature.data.source.DataMaster;
import com.nature.data.source.DataSource;
import com.nature.jt.buffer.BoxDouble;
import lombok.Data;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName MockExchange
 * @Description: 模拟交易所发布交易数据
 * @Author Tender
 * @Time 2021/6/13 17:18
 * @Version 1.0
 * @Since 1.8
 **/
public class MockExchange {
    /**
     * 文件数据源
     */
    private DataSource<String> fileSource;

    /**
     * 文件标题
     */
    private final int fileHeaders = 1;

    /**
     * 文件分隔符
     */
    private final String fileSeparator = ",";

    /**
     * 已处理数据数量
     */
    private int fileProcessed = 0;

    /**
     * 发布中心
     */
    private EventBus publisher;

    /**
     * 注册到发布中心
     * @param subscriber
     */
    public void registry(Object subscriber) {
        publisher.register(subscriber);
    }

    /**
     * 交易中心传输数据格式
     */
    @Data
    public static class TickDTO {
        private Double close;
    }

    /**
     * 对外运行接口
     */
    public void run() {
        checkNotNull(fileSource, "fileSource shoud not be null");

        fileProcessed = 0;
        fileSource.forAll(this::process);
    }

    /**
     * 处理数据
     * @param oneTick
     */
    private void process(String oneTick) {
        if((fileProcessed++) >= fileHeaders) {
            publisher.post(parseTick(oneTick, fileSeparator));
        }
    }

    /**
     * 解析数据
     * @param oneTick
     * @param separator
     * @return
     */
    public static TickDTO parseTick(String oneTick, String separator) {
        String[] fields = oneTick.split(separator);

        TickDTO dto = new TickDTO();
        dto.setClose(Double.parseDouble(fields[0]));

        return dto;
    }

    /**
     * Builder's set fileSource
     * @param fullFile
     * @return
     */

    private String fileName;

    public static MockExchange builder() {
        return new MockExchange();
    }

    public MockExchange setFile(String fileName) {
        this.fileName = checkNotNull(fileName, "fileName shoud not be null");
        return this;
    }

    public MockExchange build() {
        this.fileSource = DataMaster.makeCsvData(this.fileName);
        return this;
    }
}
