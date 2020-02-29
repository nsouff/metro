package fr.univparis.metro;
import java.util.*;

public class Pair<T>{
  private T obj;
  private Double value;

  public Pair(T o, Double v){
    obj=o;
    value=v;
  }

  public T getObj(){
    return obj;
  }
  public Double getValue(){
    return value;
  }
  public void setValue(Double d){
    value=d;
  }


}
