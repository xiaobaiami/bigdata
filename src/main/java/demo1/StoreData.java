package demo1;

import java.io.Serializable;

/**
 * 数据节点
 */
public class StoreData implements Serializable {

    private int id;
    private String name;

    public StoreData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public StoreData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StoreData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
