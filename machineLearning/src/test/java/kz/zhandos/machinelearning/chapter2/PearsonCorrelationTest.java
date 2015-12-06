package kz.zhandos.machinelearning.chapter2;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.Test;

import kz.zhandos.machinelearning.chapter2.Db;
import kz.zhandos.machinelearning.chapter2.PearsonCorrelation;

public class PearsonCorrelationTest {

  @Test
  public void mainTest() {
    assertThat(PearsonCorrelation.distance(Db.CRITICS, "Lisa Rose", "Gene Seymour"))
        .isEqualTo(0.39605901719066977d);
  }

  @Test(timeOut = 1000)
  public void topTest() {
    System.out.println(PearsonCorrelation.topMathes(Db.CRITICS, "Toby", 5));
  }

  @Test(timeOut = 1000)
  public void itemTest() {
    System.out.println(PearsonCorrelation.calculateSimilarItems(Db.MOVIES));
  }

}
