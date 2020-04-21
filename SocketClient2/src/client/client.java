package client;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
public class client {
	public static ConcurrentHashMap<String, String> clientMap = new ConcurrentHashMap<>();
	public static void main(String[] args) {
		moreclient();
	}
    //多线程服客户端
    public static void moreclient(){
    	Scanner sc = new Scanner(System.in);
        try {
           // System.out.println("客户端启动...............");
            while(true)
            {		
            	System.out.println("请输入客户端ID：");
            	String clientId = sc.nextLine();
            	System.out.println("请输入服务器IP地址：");
            	String ip = sc.nextLine();
            	System.out.println("请输入服务器端口号：");
            	String port = sc.nextLine();
            	//启动客户端线程
                new ClientThread(clientId,ip,port).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
