package fr.univparis.metro;
import java.util.Scanner;

public class Initialize{

    public static String whichCity(){
        Scanner sc = new Scanner(System.in);
        System.out.println("In which city are you ?");
        String str = sc.nextLine();
        while(!Configuration.getCities().containsKey(str)){
            System.out.println("Wrong city name, try again");
            str = sc.nextLine();
        }
        return str;
    }

    public static String whereFrom(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Where are you ?");
        return sc.nextLine();
    }

    public static String whereTo(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Where do you want to go ?");
        return sc.nextLine();
    }

    public static String[] store(){
        String[] ret = {whichCity(), whereFrom(), whereTo()};
        return ret;
    }
}