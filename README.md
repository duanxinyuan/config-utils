# config-utils
# 配置文件工具类

* ConfigUtils，支持读取xml、properties、yml、yaml四种配种文件，
* 默认加载application.properties、application.yml、application.yaml三个配置文件
* 可手动选择load其他配置文件，也可以通过配置选择加载其他配置文件

## Maven依赖：
```xml
<dependencies>
<dependency>
    <groupId>com.github.duanxinyuan</groupId>
    <artifactId>util-config</artifactId>
      <version>1.4.0</version>
</dependency>

<dependency>
    <groupId>com.github.duanxinyuan</groupId>
    <artifactId>util-config-scala</artifactId>
    <version>1.4.0</version>
</dependency>
</dependencies>
```

## 选择加载配置文件

在application.properties/xml/yml/yaml中加入如下配置
```properties
# 需要额外加载的配置文件，多个以逗号隔开
config.files=memory-config.properties,log4j2.xml
```

## Java使用示例

```java
import com.dxy.library.json.jackson.JacksonUtil;
import com.dxy.library.util.config.ConfigUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author duanxinyuan
 * 2018/8/6 12:54
 */
public class ConfigTest {

    @Test
    public void test() {
        Map<String, Object> propertiesMap = ConfigUtils.loadAsMap("application.properties");
        Map<String, Object> ymlMap = ConfigUtils.loadAsMap("application.yml");
        Map<String, Object> yamlMap = ConfigUtils.loadAsMap("application.yaml");
        Map<String, Object> xmlMap = ConfigUtils.loadAsMap("application.xml");

        System.out.println(JacksonUtil.to(propertiesMap));
        System.out.println(JacksonUtil.to(ymlMap));
        System.out.println(JacksonUtil.to(yamlMap));
        System.out.println(JacksonUtil.to(xmlMap));

        Assert.assertEquals(propertiesMap.size(), ymlMap.size());
        Assert.assertEquals(propertiesMap.size(), yamlMap.size());
        Assert.assertEquals(propertiesMap.size(), xmlMap.size());

        propertiesMap.forEach((k, v) -> {
            //properties和xml读取出来的值全部是String，yml识别了数据类型
            Assert.assertNotNull(v);
            Assert.assertEquals(v, (ymlMap.get(k) instanceof String) ? ymlMap.get(k) : String.valueOf(ymlMap.get(k)));
            Assert.assertEquals(v, (yamlMap.get(k) instanceof String) ? yamlMap.get(k) : String.valueOf(yamlMap.get(k)));
            Assert.assertEquals(v, xmlMap.get(k));
        });
    }

    @Test
    public void testGet() throws ParseException {
        //获取配置
        System.out.println(ConfigUtils.getAsString("test.name"));
        System.out.println(ConfigUtils.get("test.age", Long.class));
        System.out.println(JacksonUtil.to(ConfigUtils.get("test.info.gameinfo", GameInfo.class)));
        System.out.println(ConfigUtils.getAsInt("test.info.game.registYear"));

        //获取配置（以Key为前缀，获取所有符合规则的config）
        System.out.println(JacksonUtil.to(ConfigUtils.getConfigs("test.info.game")));

        //获取某个前缀的 所有配置信息， key包含前缀
        Properties properties = ConfigUtils.getAsProperties("test");
        System.out.println(properties);
        System.out.println(JacksonUtil.to(properties.getProperty("test.info.gameinfo")));

        //获取某个前缀的 所有配置信息， key不包含前缀
        Properties moduleProp = ConfigUtils.getConfigAsProperties("test");
        System.out.println(moduleProp);

        //获取配置（以Key为前缀，获取所有符合规则的config，转化为对象）
        GameInfo gameInfo = ConfigUtils.getConfigAsObject("test.info.game", GameInfo.class);
        System.out.println(JacksonUtil.to(gameInfo));
        Assert.assertNotNull(gameInfo);

        Assert.assertNotNull(gameInfo.getName());
        Assert.assertNotNull(gameInfo.getAge());
        Assert.assertNotNull(gameInfo.getLevel());
        Assert.assertNotNull(gameInfo.getRegistYear());
        Assert.assertNotNull(gameInfo.getRegistTime());

        Assert.assertEquals(gameInfo.getName(), ConfigUtils.getAsString("test.info.game.name"));
        Assert.assertEquals(gameInfo.getAge(), (Integer) ConfigUtils.getAsInt("test.info.game.age"));
        Assert.assertEquals(gameInfo.getLevel(), ConfigUtils.getAsString("test.info.game.level"));
        Assert.assertEquals(gameInfo.getRegistYear(), (Integer) ConfigUtils.getAsInt("test.info.game.registYear"));
        Assert.assertEquals(gameInfo.getRegistTime(), DateUtils.parseDate(Objects.requireNonNull(ConfigUtils.getAsString("test.info.game.registTime")), "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testGetConfigAsObject() {
        ConfigUtils.load("memory-config.properties");
        MemoryConfig config = ConfigUtils.getConfigAsObject("memory.config.app", MemoryConfig.class);
        System.out.println(JacksonUtil.to(config));
        Assert.assertNotNull(config);
    }

    @Test
    public void getAllConfig() {
        Map<String, Object> allConfigAsMap = ConfigUtils.getAllConfigAsMap();
        System.out.println(JacksonUtil.to(allConfigAsMap));
        Properties allConfigAsProperties = ConfigUtils.getAllConfigAsProperties();
        System.out.println(allConfigAsProperties);

        Assert.assertEquals(allConfigAsMap.size(), allConfigAsProperties.size());
    }

    @Test
    public void testLoad() {
        Map<String, Object> hashMap = ConfigUtils.loadAsMap("memory-config.properties");
        System.out.println(JacksonUtil.to(hashMap));

        Properties properties = ConfigUtils.loadAsProperties("memory-config.properties");
        System.out.println(properties);

        Assert.assertEquals(hashMap.size(), properties.size());
    }

}
```

## Scala 使用示例

```scala
import java.util.{Date, Objects}

import com.dxy.library.json.jackson.JacksonUtil
import com.dxy.library.util.config.ConfigUtils
import org.apache.commons.configuration2.plist.ParseException
import org.apache.commons.lang3.time.DateUtils
import org.junit.{Assert, Test}

/**
 * @author duanxinyuan
 * 2020/1/13 22:37
 */
class ConfigTest {

  @Test def test(): Unit = {
    val propertiesMap = ConfigUtils.loadAsMap("application.properties")
    val ymlMap = ConfigUtils.loadAsMap("application.yml")
    val yamlMap = ConfigUtils.loadAsMap("application.yaml")
    val xmlMap = ConfigUtils.loadAsMap("application.xml")

    System.out.println(JacksonUtil.to(propertiesMap))
    System.out.println(JacksonUtil.to(ymlMap))
    System.out.println(JacksonUtil.to(yamlMap))
    System.out.println(JacksonUtil.to(xmlMap))

    Assert.assertEquals(propertiesMap.size, ymlMap.size)
    Assert.assertEquals(propertiesMap.size, yamlMap.size)
    Assert.assertEquals(propertiesMap.size, xmlMap.size)

    propertiesMap.forEach((k: String, v: Any) => {
      def foo(k: String, v: Any) = { //properties和xml读取出来的值全部是String，yml识别了数据类型
        Assert.assertNotNull(v)
        Assert.assertEquals(v, if (ymlMap.get(k).isInstanceOf[String]) ymlMap.get(k)
        else String.valueOf(ymlMap.get(k)))
        Assert.assertEquals(v, if (yamlMap.get(k).isInstanceOf[String]) yamlMap.get(k)
        else String.valueOf(yamlMap.get(k)))
        Assert.assertEquals(v, xmlMap.get(k))
      }

      foo(k, v)
    })
  }

  @Test
  @throws[ParseException]
  def testGet(): Unit = {
    System.out.println(ConfigUtils.getAsString("test.name"))
    System.out.println(ConfigUtils.get("test.age", classOf[Long]))
    System.out.println(JacksonUtil.to(ConfigUtils.get("test.info.gameinfo", classOf[ScalaGameInfo])))
    System.out.println(ConfigUtils.getAsInt("test.info.game.registYear"))
    System.out.println(JacksonUtil.to(ConfigUtils.getConfigs("test.info.game")))

    val properties = ConfigUtils.getAsProperties("test")
    System.out.println(properties)
    System.out.println(JacksonUtil.to(properties.getProperty("test.info.gameinfo")))
    val moduleProp = ConfigUtils.getConfigAsProperties("test")
    System.out.println(moduleProp)

    val gameInfo = ConfigUtils.getConfigAsObject("test.info.game", classOf[ScalaGameInfo])
    System.out.println(JacksonUtil.to(gameInfo))

    Assert.assertNotNull(gameInfo)
    Assert.assertNotNull(gameInfo.name)
    Assert.assertNotNull(gameInfo.age)
    Assert.assertNotNull(gameInfo.level)
    Assert.assertNotNull(gameInfo.registYear)
    Assert.assertNotNull(gameInfo.registTime)

    Assert.assertEquals(gameInfo.name, ConfigUtils.getAsString("test.info.game.name"))
    Assert.assertEquals(gameInfo.age, ConfigUtils.getAsInt("test.info.game.age").asInstanceOf[Integer])
    Assert.assertEquals(gameInfo.level, ConfigUtils.getAsString("test.info.game.level"))
    Assert.assertEquals(gameInfo.registYear, ConfigUtils.getAsInt("test.info.game.registYear").asInstanceOf[Integer])
    Assert.assertEquals(gameInfo.registTime, DateUtils.parseDate(Objects.requireNonNull(ConfigUtils.getAsString("test.info.game.registTime")), "yyyy-MM-dd HH:mm:ss"))
  }

  @Test def testGetConfigAsObject(): Unit = {
    ConfigUtils.load("memory-config.properties");
    val config = ConfigUtils.getConfigAsObject("memory.config.app", classOf[ScalaMemoryConfig])
    System.out.println(JacksonUtil.to(config))
    Assert.assertNotNull(config)
  }

  @Test def getAllConfig(): Unit = {
    val allConfigAsMap = ConfigUtils.getAllConfigAsMap
    System.out.println(JacksonUtil.to(allConfigAsMap))
    val allConfigAsProperties = ConfigUtils.getAllConfigAsProperties
    System.out.println(allConfigAsProperties)
    Assert.assertEquals(allConfigAsMap.size, allConfigAsProperties.size)
  }

  @Test def testLoad(): Unit = {
    val hashMap = ConfigUtils.loadAsMap("memory-config.properties")
    System.out.println(JacksonUtil.to(hashMap))
    val properties = ConfigUtils.loadAsProperties("memory-config.properties")
    System.out.println(properties)
    Assert.assertEquals(hashMap.size, properties.size)
  }

}

case class ScalaGameInfo(
                          var name: String,
                          var age: Int,
                          var level: String,
                          var registYear: Int,
                          var registTime: Date
                        )

case class ScalaMemoryConfig(
                              var `type`: String,
                              var initialCapacity: Int,
                              var maxCapacity: Int,
                              var expireAfterWrite: Int,
                              var expireAfterAccess: Int,
                              var refreshAfterWrite: Int,
                              var notifyZookeeperHost: String,
                              var notifyZookeeperPath: String
                            )

```