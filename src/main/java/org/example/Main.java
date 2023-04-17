package org.example;

import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;


public class Main {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        SparkSession spark = SparkSession.builder().master("local[*]")
                .appName("SparkByExamples.com")
                .getOrCreate();

        JavaRDD<String> lines = spark.read().textFile("C:\\Users\\Administrator\\.m2\\settings.xml").javaRDD();

        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }


/*
* val url = "jdbc:mysql://localhost:3306/db"
val tableName = "tablename"
// 设置连接用户&密码
val prop = new java.util.Properties
prop.setProperty("user","mysql")
prop.setProperty("password","123456")
val predicates =
  Array(
    "2018-10-01" -> "2018-11-01",
    "2018-11-02" -> "2018-12-01",
    "2018-12-02" -> "2019-01-01",
    "2019-02-02" -> "2019-03-01",
    "2019-03-02" -> "2019-04-01",
    "2019-04-02" -> "2019-05-01",
    "2019-05-02" -> "2019-06-01",
    "2019-06-02" -> "2019-07-01",
    "2019-07-02" -> "2019-08-01",
    "2019-08-02" -> "2019-09-01",
    "2019-09-02" -> "2019-10-01",
    "2019-10-02" -> "2019-11-01"
  ).map {
    case (start, end) =>
      s"cast(txntime as date) >= date '$start' " + s"AND cast(txntime as date) <= date '$end'"
  }
// 取得该表数据
val jdbcDF = spark.read.jdbc(url, tableName, predicates, prop)
// 写入到hive表
jdbcDF.write.partitionBy().mode("overwrite").format("orc")
  .saveAsTable("db.tableName")
* */


     /*   SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("jdbc").setMaster("local[4]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        SQLContext sc = new SQLContext(jsc);
        String url = "jdbc:sqlserver://192.168.1.101;DatabaseName=database;user=user;password=123456";
        String tableName = "tb_city";
        Properties connectionProperties = new Properties();
        connectionProperties.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Dataset<Row> table = sc.read().jdbc(url, tableName, "ECI", 125883650, 263780907, 3, connectionProperties).select("CityID", "IMSI", "ECI");


        //  SparkConf sparkConf = new SparkConf();
        //  sparkConf.setAppName("jdbc").setMaster("local[4]");
        //   JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        //  SQLContext sc = new SQLContext(jsc);
        //  String url ="jdbc:sqlserver://192.168.1.101;DatabaseName=database;user=user;password=123456";
        // String tableName = "tb_city";
        //Properties
        connectionProperties = new Properties();
        connectionProperties.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String[] predicates = new String[]{
                "IMSI >='105156335255615' AND IMSI <='145437785776944'",
                "IMSI >='145441560321876' AND IMSI <='145441636521493'",
                "IMSI >'145441636521493' AND IMSI <='145464988025176'",
        };
        //  DataFrame
        table = sc.read().jdbc(url, tableName, predicates, connectionProperties).select("CityID", "IMSI");
*/

        Dataset<Row> jdbcDFPara = spark.read()
                .format("jdbc")
                .option("url", "jdbc:oracle:thin:@//192.168.3.61:1521/helowin")
                .option("dbtable", "hr.JOBS")
                .option("user", "system")
                .option("password", "system")

                .option("numPartitions", 20)
                .option("partitionColumn", "MIN_SALARY")
                .option("lowerBound", 1000)
                .option("upperBound", 20000)
                .option("fetchsize", 2)
                .load();
        jdbcDFPara.show(20);


        jdbcDFPara.write().format("doris")
                .option("doris.table.identifier", "mongo_doris.data_sync_test_oracledate")
                .option("doris.fenodes", "192.168.3.61:8030")
                .option("user", "root")
                .option("password", "")
                //other options
                //specify the fields to write
                //.option("doris.write.fields","$YOUR_FIELDS_TO_WRITE")
                .save();


        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:oracle:thin:@//192.168.3.61:1521/helowin")
                .option("dbtable", "hr.JOBS")
                .option("user", "system")
                .option("password", "system")
                .load();
        jdbcDF.show(20);


        jdbcDF.write().format("doris")
                .option("doris.table.identifier", "mongo_doris.data_sync_test_oracledate")
                .option("doris.fenodes", "192.168.3.61:8030")
                .option("user", "root")
                .option("password", "")
                //other options
                //specify the fields to write
                //.option("doris.write.fields","$YOUR_FIELDS_TO_WRITE")
                .save();


        //  String[] predicates=new String[]{};
        // Properties connectionProperties=new Properties();
        //  spark.read().jdbc("","",predicates,connectionProperties).;
        Dataset<Row> dorisSparkDF = spark.read().format("doris")
                .option("doris.table.identifier", "mongo_doris.data_sync_test_simple")
                .option("doris.fenodes", "192.168.3.61:8030")
                .option("user", "root")
                .option("password", "")
                .load();

        dorisSparkDF.show(5);


   /*     // batch sink
         mockDataDF = new List[]{
                // (3, "440403001005", "21.cn"),
       // (1, "4404030013005", "22.cn"),
     //   (33, null, "23.cn")
}.toDF("id", "mi_code", "mi_name");
        mockDataDF.show(5);*/

        dorisSparkDF.write().format("doris")
                .option("doris.table.identifier", "mongo_doris.data_sync_test_simple")
                .option("doris.fenodes", "192.168.3.61:8030")
                .option("user", "root")
                .option("password", "")
                //other options
                //specify the fields to write
                //.option("doris.write.fields","$YOUR_FIELDS_TO_WRITE")
                .save();

     /*   Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql:dbserver")
                .option("dbtable", "schema.tablename")
                .option("user", "username")
                .option("password", "password")
                .load();

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "username");
        connectionProperties.put("password", "password");
        Dataset<Row> jdbcDF2 = spark.read()
                .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

// Saving data to a JDBC source
        jdbcDF.write()
                .format("jdbc")
                .option("url", "jdbc:postgresql:dbserver")
                .option("dbtable", "schema.tablename")
                .option("user", "username")
                .option("password", "password")
                .save();

        jdbcDF2.write()
                .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

// Specifying create table column data types on write
        jdbcDF.write()
                .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)")
                .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);
*/
        //Thread.sleep(30 * 60 * 60 * 1000);
        spark.close();
    }
}