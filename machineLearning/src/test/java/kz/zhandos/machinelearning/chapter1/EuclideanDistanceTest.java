package kz.zhandos.machinelearning.chapter1;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class EuclideanDistanceTest {

  @Test(timeOut = 1000)
  public void mainTest() {
    assertThat(EuclideanDistance.distance(Db.CRITICS, "Lisa Rose", "Gene Seymour"))
        .isEqualTo(0.14814814814814814d);
  }

  @Test(timeOut = 1000)
  public void topTest() {
    System.out.println(EuclideanDistance.topMathes(Db.CRITICS, "Toby", 5));
  }

  @Test(timeOut = 1000)
  public void itemTest() {
    System.out.println(EuclideanDistance.calculateSimilarItems(Db.MOVIES));
  }


}
