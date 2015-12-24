package kz.zhandos.machinelearning.chapter7;

import java.util.Map;

public class DecisionNode {

  public Integer col;
  public Object value;
  public Map<Object, Integer> results;
  public DecisionNode tb;
  public DecisionNode fb;


  public DecisionNode(Integer col, Object value, Map<Object, Integer> results, DecisionNode tb,
      DecisionNode fb) {
    col = col == null ? -1 : col;
    this.col = col;
    this.value = value;
    this.results = results;
    this.tb = tb;
    this.fb = fb;
  }


  @Override
  public String toString() {
    return "DecisionNode [col=" + col + ", value=" + value + ", results=" + results + ", tb=" + tb
        + ", fb=" + fb + "]";
  }



}
