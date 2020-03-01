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
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Pair other = (Pair) obj;
	return Objects.equals(obj, other.obj) && Objects.equals(value, other.value);
    }

    
 


}
