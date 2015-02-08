package com.dean.course6.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 15/1/11.
 */
public class HBaseTest {

    private static Configuration conf = null;

    static {
        Configuration HBASE_CONFIG = new Configuration();
        HBASE_CONFIG.set("hbase.zookeeper.quorum", "127.0.0.1");
        HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
        conf = HBaseConfiguration.create(HBASE_CONFIG);
    }

    public static void createTable(String tableName, String[] familys) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table " + tableName + " is already exists!");
            return;
        }

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        for (int i = 0; i < familys.length; i++) {
            tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
        }
        admin.createTable(tableDescriptor);
        System.out.println("create table " + tableName + " success!");
    }


    public static void deleteTable(String tableName) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
        System.out.println("delete table " + tableName + " success!");
    }

    public static void addRecord(String tableName, String rowKey, String family, String qualifier, String value) throws Exception {
        HTable table = new HTable(conf, tableName);
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        table.put(put);
        System.out.println("insert record " + rowKey + " into table " + tableName + " success!");
    }

    public static void deleteRecord(String tableName, String rowkey) throws Exception {
        HTable table = new HTable(conf, tableName);
        List list = new ArrayList<>();
        Delete del = new Delete(rowkey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("delete record " + rowkey + " success!");
    }

    public static void getOneRecord(String tableName, String rowKey) throws Exception {
        HTable table = new HTable(conf, tableName);
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for (KeyValue kv : rs.raw()) {
            System.out.print(new String(kv.getRow()) + " ");
            System.out.print(new String(kv.getFamily()) + " ");
            System.out.print(new String(kv.getQualifier()) + " ");
            System.out.print(kv.getTimestamp() + " ");
            System.out.println(new String(kv.getRow()));
        }
    }

    public static void getAllRecord(String tableName) throws Exception {
        HTable table = new HTable(conf, tableName);
        Scan scan = new Scan();
        ResultScanner rss = table.getScanner(scan);
        for (Result rs : rss) {
            for (KeyValue kv : rs.raw()) {
                System.out.print(new String(kv.getRow()) + " ");
                System.out.print(new String(kv.getFamily()) + " ");
                System.out.print(new String(kv.getQualifier()) + " ");
                System.out.print(kv.getTimestamp() + " ");
                System.out.println(new String(kv.getRow()));
            }
        }
    }

    public static void  main (String [] agrs) {
        try {
            String tablename = "scores";
            String[] familys = {"grade", "course"};
            HBaseTest.createTable(tablename, familys);

            //add record zkb
            HBaseTest.addRecord(tablename,"zhangsan","grade","","5");
            HBaseTest.addRecord(tablename,"zhangsan","course","","90");
            HBaseTest.addRecord(tablename,"zhangsan","course","math","97");
            HBaseTest.addRecord(tablename,"zhangsan","course","art","87");
            //add record  baoniu
            HBaseTest.addRecord(tablename,"lisi","grade","","4");
            HBaseTest.addRecord(tablename,"lisi","course","math","89");

            System.out.println("===========get one record========");
            HBaseTest.getOneRecord(tablename, "zhangsan");

            System.out.println("===========show all record========");
            HBaseTest.getAllRecord(tablename);

            System.out.println("===========del one record========");
            HBaseTest.deleteRecord(tablename, "lisi");
            HBaseTest.getAllRecord(tablename);

            System.out.println("===========show all record========");
            HBaseTest.getAllRecord(tablename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
