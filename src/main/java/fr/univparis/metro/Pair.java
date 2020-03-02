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

    @Override
    public int hashCode() {
	return Objects.hash(obj, value);
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (getClass() != o.getClass())
	    return false;

	Pair<?, ?> other = (Pair<?, ?>) o;
  return other.obj.equals(this.obj) && other.value.equals(this.value);
    }





}
