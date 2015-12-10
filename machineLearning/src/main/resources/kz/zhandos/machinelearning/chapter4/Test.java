package kz.zhandos.machinelearning.chapter4;

public class Test {
  public static void main(String[] args) throws Exception {
    System.out.println(Test.class.getClassLoader()
        .getResource("kz/zhandos/machinelearning/liquibase/databasechangelog.xml"));
  }
}
