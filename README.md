# april-sensitive

> 一款非常小巧的脱敏工具类

## Mask 注解

参数说明：

* prefixNoMaskLen：前置不需要打码的长度
* suffixNoMaskLen：后置不需要打码的长度

* maskStr：用什么打码

### 使用说明：

> 引入 maven 依赖

~~~xml
<dependency>
    <groupId>com.mobaijun</groupId>
    <artifactId>april-sensitive</artifactId>
    <version>${lasttest.version}</version>
</dependency>
~~~

  在需要实现脱敏的实体类属性添加 @Mask 注解，重写当前实体类的 toString() 方法;

~~~java
@Override
public String toString() {
    // 重写toString（）方法
    return new MaskToStringBuilder(this).toString();
}
~~~

## 测试

~~~java
public class AppTest {
    @Mask(prefixNoMaskLen = 3, suffixNoMaskLen = 4)
    private String arced;

    @Mask(prefixNoMaskLen = 1)
    private String name;

    @Mask(prefixNoMaskLen = 4)
    private String birthday;

    @Mask(prefixNoMaskLen = 1, suffixNoMaskLen = 8, maskStr = "-")
    private String email;

    public String getArced() {
        return arced;
    }

    public void setArced(String arced) {
        this.arced = arced;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        // 重写toString（）方法
        return new MaskToStringBuilder(this).toString();
    }

    public static void main(String[] args) {
        AppTest testDemo = new AppTest();
        testDemo.setArced("4547665157465765");
        testDemo.setName("墨白君");
        testDemo.setEmail("mobaijun8@163.com");
        testDemo.setBirthday(LocalDateTime.now().toString());
        System.out.println("testDemo = " + testDemo);
    }
}
~~~

## 运行效果：

~~~bash
arced=454*********5765,name=墨**,birthday=2022*******************,email=m--------@163.com
~~~
