package master;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkServer {
    private volatile boolean running = false;
    private ZkClient zkClient;
    private static final String MASTER_PATH = "/master";
    private IZkDataListener dataListener;
    private RunningData serverData;
    private RunningData masterData;
    private ScheduledExecutorService delayExecutor
            = Executors.newSingleThreadScheduledExecutor();
    private int delayTime = 5;

    public WorkServer(final RunningData serverData) {
        this.serverData = serverData;
        this.dataListener = new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {

            }

            public void handleDataDeleted(String s) throws Exception {
                if (masterData != null &&
                        !masterData.getName().equalsIgnoreCase(serverData.getName())) {
                    takeMaster();
                } else {
                    delayExecutor.schedule(new Runnable() {
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public void start() throws Exception {
        if (running) {
            throw new Exception("");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();
    }

    private void takeMaster() {
        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println(Thread.currentThread().getName() + " " + serverData.getName() + " is master");
            delayExecutor.schedule(new Runnable() {
                public void run() {
                    if (checkMaster())
                        releaseMaster();
                }
            }, delayTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            RunningData data = zkClient.readData(MASTER_PATH);
            if (data == null)
                takeMaster();
            else
                masterData = data;
        }
    }

    private void releaseMaster() {
        if (checkMaster())
            zkClient.delete(MASTER_PATH);
    }

    private boolean checkMaster() {
        try {
            RunningData data = zkClient.readData(MASTER_PATH);
            masterData = data;
            if (masterData.getName().equalsIgnoreCase(serverData.getName()))
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }


}
