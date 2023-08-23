package club.chillrain.tomcat.webapps.user.bean;

/**
 * @author ChillRain 2023 08 12
 */
public class User {
    private Integer age;
    private String name;
    private String sex;

    public User(Integer age, String name, String sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
