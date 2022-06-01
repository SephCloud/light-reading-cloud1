package com.zealon;

import com.zealon.readingcloud.book.dao.BookMapper;
import com.zealon.readingcloud.common.pojo.book.Book;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    @Autowired
    BookMapper bookMapper;

    @Test
    public void bookContest(){

        Book book = bookMapper.selectByBookId("291659");

        System.out.println(book);

    }



}
