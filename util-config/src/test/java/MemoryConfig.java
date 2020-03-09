import lombok.Data;

@Data
public class MemoryConfig {
    private String type;

    private int initialCapacity;

    private int maxCapacity;

    private int expireAfterWrite;

    private int expireAfterAccess;

    private int refreshAfterWrite;

    private String notifyZookeeperHost;

    private String notifyZookeeperPath;


}
