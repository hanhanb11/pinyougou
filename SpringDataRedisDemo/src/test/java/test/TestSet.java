package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * redis set类型
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {

    @Autowired
    private RedisTemplate redisTemplate;


    //添加
    @Test
    public void add() {

        redisTemplate.boundSetOps("name").add("刘备");
        redisTemplate.boundSetOps("name").add("关羽");
        redisTemplate.boundSetOps("name").add("张飞");

    }

    //查询
    @Test
    public void member() {
        Set name = redisTemplate.boundSetOps("name").members();
        System.out.println(name);

    }

    //删除单个元素
    @Test
    public void remove() {
        redisTemplate.boundSetOps("name").remove("刘备");

    }

    //删除集合
    @Test
    public void del() {
        redisTemplate.delete("name");

    }


}
