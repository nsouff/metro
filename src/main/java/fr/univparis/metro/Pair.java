package fr.univparis.metro;
import java.util.*;

public class Pair<T, S>{
  private T obj;
  private S value;

  public Pair(T o, S v){
    obj=o;
    value=v;
  }

  public T getObj(){
    return obj;
  }
  public S getValue(){
    return value;
  }
  public void setValue(S d){
    value=d;
  }


}
