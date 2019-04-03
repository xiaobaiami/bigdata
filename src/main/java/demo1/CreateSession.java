package demo1;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import utils.Util;

import java.util.List;


public class CreateSession {

    ZkClient client;
    String dataUrl = "/first";

    /**
     * 会话创建
     */
    @Before
    public void getClient() {
        client = Util.getClient();
        System.out.println(client);
    }

    /**
     * 节点创建
     * 第一次返回节点的路径 /first
     * 节点已经存在，则抛出 ZkNodeExistsException 异常，运行时异常
     */
    @Test
    public void createNode() {
        StoreData data = new StoreData(1, "this is first data");
        String s = client.create(dataUrl, data, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    /**
     *
     */
    @Test
    public void addChilds() {
        StoreData data = new StoreData(2, "this is second data");
        client.createPersistentSequential(dataUrl, data);
    }

    /**
     * 获取节点
     * KeeperException$NoNodeException
     */
    @Test
    public void getData() {
        Stat stat = new Stat();
        Object o = client.readData(dataUrl, stat);
        System.out.println(o.getClass());
        System.out.println(o);
        System.out.println(stat);
    }

    /**
     * 删除节点
     */
    @Test
    public void deleteNode() {
        boolean delete = client.delete(dataUrl);
        System.out.println(delete ? "删除成功" : "删除失败");
    }

    /**
     * 获取子节点
     */
    @Test
    public void getChilds() {
        List<String> children = client.getChildren(dataUrl);
        System.out.println(children);
    }

    /**
     * 订阅子节点列表变化
     */
    @Test
    public void subChilds() {
        List<String> list = client.subscribeChildChanges(dataUrl, new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s);
                System.out.println("1. " + list);
            }
        });
        System.out.println("2. " + list);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
