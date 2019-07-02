package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * redis List类型
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestList {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 右压栈
     */
    @Test
    public void setList1() {

        redisTemplate.boundListOps("arr1").rightPush("张飞");
        redisTemplate.boundListOps("arr1").rightPush("关羽");
        redisTemplate.boundListOps("arr1").rightPush("刘备");

    }

    //查询
    @Test
    public void rangeList1() {
        List arr1 = redisTemplate.boundListOps("arr1").range(0, -1);
        System.out.println(arr1);

    }

    //删除一个
    @Test
    public void removeList1() {
        redisTemplate.boundListOps("arr1").remove(1, "刘备");
    }


    /**
     * 左压栈
     */
    @Test
    public void setList2() {

        redisTemplate.boundListOps("arr2").leftPush("张飞");
        redisTemplate.boundListOps("arr2").leftPush("关羽");
        redisTemplate.boundListOps("arr2").leftPush("刘备");
    }

    //查询
    @Test
    public void rangeList2() {
        List arr1 = redisTemplate.boundListOps("arr2").range(0, -1);
        System.out.println(arr1);
    }

    //删除一个
    @Test
    public void removeList2() {
        redisTemplate.boundListOps("arr2").remove(1, "刘备");
    }

    //删除全部
    @Test
    public void del(){
        redisTemplate.delete("arr2");
    }

}
