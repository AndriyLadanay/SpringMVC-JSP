package Pager;

import Dao.Dao;

import java.util.ArrayList;

public class Pager {
   private ArrayList<Integer> range(int start, int end){
       ArrayList<Integer> numbers = new ArrayList<>();
       for(int i = start; i <= end; i++){
           numbers.add(i);
       }
       return numbers;
   }
   public ArrayList<Integer> getPagesLinks(int page, int lastPage){
       ArrayList <Integer> pages = new ArrayList<>();
       int start = 0;
       int end = 0;
       if (page % 10 == 0){
           end = page;
           start = end - 9;
           if(lastPage < end){
               end = lastPage;
           }
       }
       else {
           start = page/10*10 + 1;
           end = start + 9;
           if(lastPage < end){
               end = lastPage;
           }
       }
       pages = range(start, end);
       return  pages;
   }

}
