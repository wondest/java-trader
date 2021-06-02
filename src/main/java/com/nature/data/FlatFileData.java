package com.nature.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName CsvDataSource1
 * @Description: TODO
 * @Author Tender
 * @Time 2021/5/29 21:46
 * @Version 1.0
 * @Since 1.8
 **/
public class FlatFileData extends AbstractDataSource.Vector {

    private final String fileName;
    private BufferedReader reader;

    public FlatFileData(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void start() {
        if(null == reader) {
            try {
                reader = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("start failed!");
            }
        }
    }

    @Override
    public void stop() {
        if(null != reader) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("stop failed");
            }
        }
    }

    private void ensureOpen() {
        checkNotNull(reader, "source is closed");
    }

    private String read() {
        String ret;
        try {
            ensureOpen();
            ret = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("read failed");
        }

        return ret;
    }

    @Override
    public void forAll(Consumer<String> feed) {
        String ret;
        start();
        while(null != (ret = read())) {
            feed.accept(ret);
        }
        stop();
    }
}
