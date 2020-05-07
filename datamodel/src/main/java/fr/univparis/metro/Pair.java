package fr.univparis.metro;
import java.util.*;

public class Pair<T, S>{
    private T obj;
    private S value;
    /**
    * Construct a Pair with an obj o and a value v
    * @param o  Object T associated to the attribute obj
    * @param v  Object S associated to the attribute value
    */
    public Pair(T o, S v){
	obj=o;
	value=v;
    }

    /**
    * Accessor to obj of the Pair
    * @return the attribute obj of the Pair
    */
    public T getObj(){
	return obj;
    }

    /**
    * Accessor to value of the Pair
    * @return the attribute value of the Pair
    */
    public S getValue(){
	return value;
    }

    /**
    * Modify the value of the Pair
    * @param d  the object which replace the value of the Pair
    */
    public void setValue(S d){
	value=d;
    }

    @Override
    /**
    * Give the hashing value of the Pair
    * @return the hashing value of the Pair
    */
    public int hashCode() {
	return Objects.hash(obj, value);
    }

    @Override
    /**
    * Verify if two Pairs are equals
    * @param o which is compare to the Pair
    * @return true if there are equals else false
    */
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
