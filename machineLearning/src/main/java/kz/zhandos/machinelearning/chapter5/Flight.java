package kz.zhandos.machinelearning.chapter5;

public class Flight {

  public String origin;
  public String destination;
  public String depart;
  public String arrive;
  public Double price;

  @Override
  public String toString() {
    return "Flight [origin=" + origin + ", destination=" + destination + ", depart=" + depart
        + ", arrive=" + arrive + ", price=" + price + "]";
  }


}
