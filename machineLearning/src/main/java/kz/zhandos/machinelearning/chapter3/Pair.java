package kz.zhandos.machinelearning.chapter3;

public class Pair<T, V> {
  T first;
  V second;

  Pair(T first, V second) {
    this.first = first;
    this.second = second;
  }

  public static <T, V> Pair<T, V> pair(T first, V second) {
    return new Pair<T, V>(first, second);
  }

}
