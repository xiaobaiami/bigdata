package utils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class Util {
    private static final String DEFAULT_SERVER_URL = "127.0.0.1:2181";

    public static ZkClient getClient() {
        return getClient(DEFAULT_SERVER_URL);
    }

    public static ZkClient getClient(String server_url) {
        ZkClient zkClient = new ZkClient(server_url,
                1000,
                1000,
                new SerializableSerializer());
        return zkClient;
    }
}
