package com.example.demo.test;

import com.example.demo.entity.Bookes;
import com.example.demo.entity.Books;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Test5 {

    @Test
    public void test_01(){
        Bookes books = getBooks();
        System.out.println(books.toString());
        List<Books> list = books.getList();
        //sublist  不影响浅复制的值
        list.subList(0,1);
        //list.add(new Books("1","2"));
        System.out.println(books.toString());

    }

    private Bookes  getBooks(){
        List<Books> list = new ArrayList<>();
        for(int i = 0 ; i< 3 ;i++){
            String uuid = UUID.randomUUID().toString().replace("-","").toUpperCase();
            String id= uuid.substring(0,5);
            String name = uuid.substring(5,10);
            Books books = new Books(id, name);
            list.add(books);
        }
        Bookes bookes = new Bookes();
        bookes.setList(list);
        return bookes;
    }
}
