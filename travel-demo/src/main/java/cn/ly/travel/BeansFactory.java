package cn.ly.travel;

import org.springframework.core.io.support.ResourcePropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author ly
 * @date 2019/2/15 10:00
 */
public class BeansFactory {
    //创建一个map集合存储Impl对象
    private static Map<String, Object> beansMap = new HashMap<String, Object>();

    public static Object getBeans(String beanName) {
        Object object = null;
        //优先Map集合获取对象
        object = beansMap.get(beanName);
        if (object == null) {
            //获取配置文件中的bean对应的路径
            ResourceBundle rb = ResourceBundle.getBundle("beans");
            String classpath = rb.getString(beanName);

            try {
                //反射机制,获取这个类的对象.
                object = Class.forName(classpath).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            //保存到map中方便下一次的获取
            beansMap.put(beanName, object);

        }
        return object;
    }
}
