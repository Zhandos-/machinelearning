package kz.zhandos.machinelearning;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.Test;

import kz.zhandos.machinelearning.Db;
import kz.zhandos.machinelearning.EucladianDistance;

public class EucladianDistanceTest {

	@Test(timeOut = 1000)
	public void mainTest() {
		assertThat(EucladianDistance.distance(Db.CRITICS, "Lisa Rose", "Gene Seymour")).isEqualTo(0.14814814814814814d);
	}

}
