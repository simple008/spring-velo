package com.nowcoder;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by lpeiz on 2016/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")    //测试用例
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Test
    public void initDatabase(){
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();  //创建了一个user 对象然后给这个对象的各个变量赋值
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);   //把赋值后的对象加到数据库里

            user.setPassword("xx");   //更新密码
            userDAO.updatePassword(user);

            Question question=new Question();
            question.setCommentCount(i);
            Date date =new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDAO.addQuestion(question);              //测questiondao 的插入功能

        }
        Assert.assertEquals("xx", userDAO.selectById(1).getPassword());  //assert是什么用   取一个出来
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));

//        System.out.print(questionDAO.selectLatestQuestions(0,0,10));  //有问题
    }
}
