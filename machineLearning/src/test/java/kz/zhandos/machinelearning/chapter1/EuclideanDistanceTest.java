package kz.zhandos.machinelearning.chapter1;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.Test;

import kz.zhandos.machinelearning.chapter1.Db;
import kz.zhandos.machinelearning.chapter1.EuclideanDistance;

public class EuclideanDistanceTest {

	@Test(timeOut = 1000)
	public void mainTest() {
		assertThat(EuclideanDistance.distance(Db.CRITICS, "Lisa Rose", "Gene Seymour")).isEqualTo(0.14814814814814814d);
	}

}
