package com.wanwan.nowcoder;

import com.wanwan.nowcoder.dao.QuestionDao;
import com.wanwan.nowcoder.dao.UserDao;
import com.wanwan.nowcoder.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FollowTests {

	@Autowired
	FollowService followService;

	@Test
	public void initDatabase() {

	}

}
