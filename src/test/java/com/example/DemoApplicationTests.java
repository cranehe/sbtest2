package com.example;

import com.example.dao.BlogMapper;
import com.example.domain.Blog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Autowired
    BlogMapper blogMapper;

    @Test
    public void contextLoads() {
        Blog blog = new Blog();
        blog.setTitle("junit test");
        blog.setAuthor("junit test author");
        blog.setPublishDate(new Date());
        int insertResult = blogMapper.insert(blog);
        log.info("insert result:",insertResult);

        Assert.assertTrue(insertResult>0);
    }

}
