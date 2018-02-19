package pager;

import java.util.ArrayList;
import java.util.List;

public class Pager {
   private ArrayList<Integer> range(int start, int end){
       ArrayList<Integer> numbers = new ArrayList<>();
       for(int i = start; i <= end; i++){
           numbers.add(i);
       }
       return numbers;
   }
   public List<Integer> getPagesLinks(int page, int lastPage){
       int start;
       int end;
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
       ArrayList<Integer> pages = range(start, end);
       return  pages;
   }
}
