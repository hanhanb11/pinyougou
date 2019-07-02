package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestHash {
    //##################hash 类型###############################
    @Autowired
    private RedisTemplate redisTemplate;


    //存值
    @Test
    public void setValue(){
        redisTemplate.boundHashOps("XiYouJi").put("a","悟空");
        redisTemplate.boundHashOps("XiYouJi").put("b","八戒");
        redisTemplate.boundHashOps("XiYouJi").put("c","沙僧");
        redisTemplate.boundHashOps("XiYouJi").put("d","唐僧");
    }
    //获取所有Key
    @Test
    public void getValue(){
        Object keys = redisTemplate.boundHashOps("XiYouJi").keys();
        System.out.println(keys);


    }
    //获取所有的值
    @Test
    public void getValue1(){
        Object values = redisTemplate.boundHashOps("XiYouJi").values();
        System.out.println(values);
    }
    //根据key获取值
    @Test
    public void getValue2(){
        Object values = redisTemplate.boundHashOps("XiYouJi").get("a");
        System.out.println(values);
    }

    //根据key删除值
    @Test
    public void deleValue1(){
      redisTemplate.boundHashOps("XiYouJi").delete("d");

    }

    //根据key删除值
    @Test
    public void dele(){
        redisTemplate.delete("XiYouJi");

    }



}
