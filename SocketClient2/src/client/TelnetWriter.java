package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

//从控制台读取用户输入命令   线程类
class TelnetWriter extends Thread {
  private PrintStream out;

  public TelnetWriter(OutputStream out) {
      this.out = new PrintStream(out);
  }
  public void run() {
      try {
          // 包装控制台输入流
          BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
          // 反复将控制台输入写到Telnet服务程序
          while (true)  
              out.println(in.readLine());
      } catch (IOException exc) {
          exc.printStackTrace();
      }
  }
}

