package han.dhll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootDemo {

    @Autowired
    Environment environment;//可以回去属性文件

    @RequestMapping("/info")
    public String info(){

        return "hello world!!"+environment.getProperty("url");
    }

    @RequestMapping("/info1")
    public String info1(){

        return "hello world!!"+environment.getProperty("url");
    }
    @RequestMapping("/info2")
    public String info2(){

        return "hello world!!"+environment.getProperty("url");
    }
}
