package id;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import utils.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.prefs.PreferenceChangeEvent;

public class IDMaker {
    private ZkClient zkClient;
    private final String server;
    private final String root;
    private final String nodeName;
    private volatile boolean running = false;
    private ExecutorService cleanExe;

    public IDMaker(String server, String root, String nodeName) {
        this.server = server;
        this.root = root;
        this.nodeName = nodeName;
    }

    public void start()throws Exception{
        if (running)
            throw new Exception("server has stated...");
        running = true;
        init();
    }
    public enum RemoveMethod{
        NONE,IMMEDIATELY,DELAY

    }
    private void init(){
        zkClient = Util.getClient();
        cleanExe = Executors.newFixedThreadPool(10);
        try{
            zkClient.createPersistent(root,true);
        }catch (ZkNodeExistsException e){
        }
    }

    private String extractId(String str){
        int index = str.lastIndexOf(nodeName);
        if (index >= 0){
            index+=nodeName.length();
            return index <= str.length()?str.substring(index):"";
        }
        return str;
    }

    public void free(){
        cleanExe.shutdown();
        try{
            cleanExe.awaitTermination(2, TimeUnit.SECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            cleanExe = null;
        }

        if (zkClient!=null){
            zkClient.close();
            zkClient=null;

        }
    }

    public String getID(RemoveMethod type){
        String full = root.concat("/").concat(nodeName);
        final String persistentSequential = zkClient.createPersistentSequential(full, null);
        if (type == RemoveMethod.IMMEDIATELY){
            zkClient.delete(persistentSequential);
        }else if (type == RemoveMethod.DELAY){
            cleanExe.execute(new Runnable() {
                public void run() {
                    zkClient.delete(persistentSequential);
                }
            });
        }
        return extractId(persistentSequential);
    }
}
