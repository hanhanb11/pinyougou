package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestValue {

    @Autowired
   private RedisTemplate redisTemplate;
    //##################string 类型###############################
    //存储
    @Test
    public void setValue(){
     redisTemplate.boundValueOps("hh").set("nn");
    }
    @Test
    public void getValue(){
        String hh = (String) redisTemplate.boundValueOps("hh").get();
        System.out.println(hh);
    }

    @Test
    public void delValue(){
        redisTemplate.delete("hh");
    }

}
