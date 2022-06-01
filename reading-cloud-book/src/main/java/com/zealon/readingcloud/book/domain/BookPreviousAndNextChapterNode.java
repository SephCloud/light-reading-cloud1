package com.zealon.readingcloud.book.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书前后章节节点
 * 图书章节数据结构，为了读取当前章时，能够方便的进行上一章、下一章
 * 这里每个章节节点数据划分为当前章、上一章、下一章，形成一个基础的双向链表
 * @author hasee
 */
@Data
public class BookPreviousAndNextChapterNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 章节Id
     */
    private Integer id;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 上一章
     */
    BookPreviousAndNextChapterNode pre;

    /**
     * 下一章
     */
    BookPreviousAndNextChapterNode next;

    public BookPreviousAndNextChapterNode(){};

    public BookPreviousAndNextChapterNode(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookPreviousAndNextChapterNode(BookPreviousAndNextChapterNode chapterNode){

        this.id = chapterNode.getId();
        this.name = chapterNode.getName();
    }


}
