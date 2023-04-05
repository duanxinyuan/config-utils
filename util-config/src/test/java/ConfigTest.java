import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.dxy.library.json.jackson.JacksonUtil;
import com.dxy.library.util.config.ConfigUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author duanxinyuan
 * 2018/8/6 12:54
 */
public class ConfigTest {
    static {
        System.setProperty("spring.profiles.active", "dev");
    }

    @Test
    public void test() {
        Map<String, Object> propertiesMap = ConfigUtils.loadAsMap("application.properties");
        Map<String, Object> ymlMap = ConfigUtils.loadAsMap("application.yml");
        Map<String, Object> yamlMap = ConfigUtils.loadAsMap("application.yaml");
        Map<String, Object> xmlMap = ConfigUtils.loadAsMap("application.xml");

        //        System.out.println(JacksonUtil.to(propertiesMap));
        //        System.out.println(JacksonUtil.to(ymlMap));
        System.out.println(JacksonUtil.to(yamlMap));
        //        System.out.println(JacksonUtil.to(xmlMap));

        Assert.assertEquals(propertiesMap.size(), ymlMap.size());
        Assert.assertEquals(propertiesMap.size(), yamlMap.size());
        Assert.assertEquals(propertiesMap.size(), xmlMap.size());

        propertiesMap.forEach((k, v) -> {
            //properties和xml读取出来的值全部是String，yml识别了数据类型
            Assert.assertNotNull(v);
            Assert.assertEquals(v, (ymlMap.get(k) instanceof String) ? ymlMap.get(k) : String.valueOf(ymlMap.get(k)));
            Assert.assertEquals(v,
                (yamlMap.get(k) instanceof String) ? yamlMap.get(k) : String.valueOf(yamlMap.get(k)));
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
        Assert.assertEquals(gameInfo.getAge(), (Integer)ConfigUtils.getAsInt("test.info.game.age"));
        Assert.assertEquals(gameInfo.getLevel(), ConfigUtils.getAsString("test.info.game.level"));
        Assert.assertEquals(gameInfo.getRegistYear(), (Integer)ConfigUtils.getAsInt("test.info.game.registYear"));
        Assert.assertEquals(gameInfo.getRegistTime(),
            DateUtils.parseDate(Objects.requireNonNull(ConfigUtils.getAsString("test.info.game.registTime")),
                "yyyy-MM-dd HH:mm:ss"));

        //Profile
        Assert.assertEquals(ConfigUtils.getAsString("test.name.dev"), "duanxinyuan");
        Assert.assertEquals(ConfigUtils.getAsString("test.name.dev1"), "duanxinyuan");
        Assert.assertEquals(ConfigUtils.getAsString("test.name.dev2"), "duanxinyuan");
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
