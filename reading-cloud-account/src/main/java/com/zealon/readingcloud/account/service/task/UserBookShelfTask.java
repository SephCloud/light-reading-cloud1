package com.zealon.readingcloud.account.service.task;

import com.zealon.readingcloud.account.dao.UserBookShelfMapper;
import com.zealon.readingcloud.common.pojo.account.UserBookshelf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 书架同步任务
 * @author hasee
 */
public class UserBookShelfTask implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookShelfTask.class);

    /** 处理类型：1.新增 2，修改 3.删除 */
    private Integer syncType;
    private UserBookshelf bookshelf;
    private UserBookShelfMapper bookshelfMapper;
    private Integer userId;

    @Override
    public void run() {

        try {
            //处理类型为新增
            if(1 == syncType){
                bookshelf.setUserId(userId);

                //将bookshelf插入User_bookShelf表
                bookshelfMapper.insert(bookshelf);

                //处理类型为修改
            }else if(syncType == 2){

                bookshelf.setUserId(userId);

                //更新user_bookshelf中bookshelf内容
                bookshelfMapper.updateByUserIdAndBookId(bookshelf);

                //处理类型为删除
            }else if(syncType == 3){

                //删除user_bookshelf中bookshelf的内容
                bookshelfMapper.deleteById(bookshelf.getId());
            }


        }catch (Exception e){

            LOGGER.error("书架同步失败，同步类型[{}]，异常：{}",syncType, e);
        }

    }

    public UserBookShelfTask(){}

    public UserBookShelfTask(Integer syncType, UserBookshelf userBookshelf, UserBookShelfMapper userBookShelfMapper, Integer userId){

        this.syncType = syncType;
        this.bookshelf = userBookshelf;
        this.bookshelfMapper = userBookShelfMapper;
        this.userId = userId;
    }


}
