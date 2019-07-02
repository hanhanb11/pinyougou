package han.dhl.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

//freemarker demo
public class FreemarkerDemo {
    public static void main(String[] args) throws IOException, TemplateException {

        //1.创建工具类
        Configuration configuration = new Configuration();
        //2.加载模板所在文件目录
        configuration.setDirectoryForTemplateLoading(new File("I:\\pinyougou\\SprFreemarkerDemo\\src\\main\\resources"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //4.获取模板
        Template template = configuration.getTemplate("test.ftl");
        //5.创建数据类型，可以使对象，可以是map
        Map map = new HashMap();
        map.put("name", "张三");
        map.put("message", "换用来到FreeMarker的世界");
        map.put("success", false);
        map.put("today",new Date());//时间
        map.put("point",1234567);//数字，号分割，或不分割
        map.put("QQ","hh");


        List stu = new ArrayList();

        Map stu1 = new HashMap();
        stu1.put("name", "张三");
        stu1.put("num", "001");

        Map stu2 = new HashMap();
        stu2.put("name", "李四");
        stu2.put("num", "002");

        Map stu3 = new HashMap();
        stu3.put("name", "王五");
        stu3.put("num", "003");

        stu.add(stu1);
        stu.add(stu2);
        stu.add(stu3);

        map.put("stu", stu);


        //6创建一个输出流
        Writer out = new FileWriter("H:\\test.html");
        //7.输出
        template.process(map, out);
        //关闭流
        out.close();


    }
}
