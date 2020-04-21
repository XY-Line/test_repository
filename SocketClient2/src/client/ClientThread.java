package client;


import java.net.Socket;


public class ClientThread extends Thread {
	private String clientId;
    private String ip;
    private String port;
    Socket clientSocket = null;
    ClientThread(String clientId,String ip,String port)
    {
    	this.clientId = clientId;
        this.ip=ip;
        this.port=port;
    }
    
    //线程运行实体
    public void run() {
		//connect and communicate
		try {
			//connect
			clientSocket = new Socket(ip, Integer.parseInt(port));
			System.out.println("客户端启动成功....");
			client.clientMap.put(clientId, ip);
			System.out.println("当前所有客户端:"+client.clientMap);
            // 创建一个写线程
            //new TelnetWriter(clientSocket.getOutputStream()).start();
            // 创建一个读线程
            new TelnetReader(clientSocket.getInputStream(),clientId).start();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("客户端启动失败...请检查IP地址或者端口号...");
		}
    }

}
