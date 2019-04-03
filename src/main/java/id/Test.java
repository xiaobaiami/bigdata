package id;

public class Test {
    public static void main(String[] args) throws  Exception{
        IDMaker node = new IDMaker(null, "/ids", "node");
        node.start();
        for (int n = 0; n < 109; n++) {
            System.out.println(node.getID(IDMaker.RemoveMethod.DELAY));
        }
        node.free();
    }
}
