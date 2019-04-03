package master;

import org.I0Itec.zkclient.ZkClient;
import utils.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            WorkServer workServer = new WorkServer(new RunningData(i, "client$" + i));
            ZkClient client = Util.getClient();
            workServer.setZkClient(client);
            workServer.start();
        }

        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
