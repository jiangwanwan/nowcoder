package com.wanwan.nowcoder;

import com.wanwan.nowcoder.dao.QuestionDao;
import com.wanwan.nowcoder.dao.UserDao;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDataTests {
	@Autowired
	UserDao userDao;

	@Autowired
	QuestionDao questionDao;

	@Test
	public void initDatabase() {

		Random random = new Random();
//		for (int i = 0; i < 10; i ++){
////			User user = new User();
////			user.setName(String.format("USER%d",i));
////			user.setPassword("");
////			user.setSalt("");
////			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
////			userDao.addUser(user);
//
//			Question question = new Question();
//			question.setTitle(String.format("TITLE{%d}", i));
//			question.setContent(String.format("CONTENT{%d}", i));
//			question.setUserId(i+1);
//			Date date = new Date();
//			date.setTime(date.getTime() + 1000*3600*i);
//			question.setCreatedDate(date);
//			question.setCommentCount(i);
//			questionDao.addQuestion(question);
//		}

//		System.out.println(questionDao.selectLatestQuestions(2,0,10));

		System.out.println(userDao.selectById(10));


	}

}
