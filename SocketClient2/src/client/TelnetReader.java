package client;

import org.omg.CORBA.ARG_OUT;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import static utils.change_util.*;

//将响应数据打印到控制台   线程类
class TelnetReader extends Thread {
    private InputStream in;
    private String clientId;
    byte[] bytes = new byte[1024];
    public String retrunMsg = "";

    public TelnetReader(InputStream in, String clientId) {
        this.in = in;
        this.clientId = clientId;
    }

    public void run() {
        try {
            // 反复将Telnet服务程序的反馈信息显示在控制台屏幕上
            while (true) {
                int len = in.read(bytes);
                if (len != -1) {
                    // 从Telnet服务程序读取数据
                    System.out.println(len);
                    //传输的实际byte[]
                    byte[] buff = new byte[len];
                    for (int i = 0; i < buff.length; i++) {
                        buff[i] = bytes[i];
                    }

                    ByteBuffer buffer = ByteBuffer.allocate(buff.length - 6);
                    buffer.put(buff, 6, buffer.limit());
                    byte[] result = buffer.array();
                    // System.out.println(result[0]);
                    Short order = twoByteAdd(result[1], result[0]);
                    doFunction(order, result);
                    // 将数据显示在控制台屏幕上
                    System.out.println("客户端:" + clientId + "  接收到的数据:" + Arrays.toString(buff));
                } else {
                    System.out.println("客户端:" + clientId + "  断开连接....");
                    break;
                }
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * 处理接收到的信息
     *
     * @param order
     * @param buffer
     */

    public String doFunction(short order, byte[] result) {
        switch (order) {
           /* case 0:
                //;
                System.out.println("功能分类: 客户端请求登录(0)");
                login(order);
                break;*/
            case 1:
                //;
                System.out.println("功能分类: 客户端请求登录应答(1)");
                loginResponse(order, result);
                break;
            case 2:
                //;
                System.out.println("功能分类: 客户端请求注销(2)");
                logout(order);
                break;
            case 3:
                //;
                System.out.println("功能分类: 服务器广播关闭(3)");
                OffBroadcast(order, result);
                break;
            case 10:
                //;
                System.out.println("功能分类: 系统标识(10)");
                systemID(order, result);
                break;
            case 100:
                //;
                System.out.println("功能分类: 焊接状态通知(100)");
                weldStatus(order, result);
                break;
            case 101:
                //;
                System.out.println("功能分类: 日志通知(101");
                logNotice(order, result);
                break;
            case 102:
                //;
                System.out.println("功能分类: 参数配置通知(102)");
                parameter(order, result);
                break;
            case 103:
                //;
                System.out.println("功能分类: 反馈值通知(103)");
                feedbackValue(order, result);
                break;
            case 104:
                //;
                System.out.println("功能分类: 编程值通知(104)");
                programValue(order, result);
                break;
            case 105:
                //;
                System.out.println("功能分类: 调整值通知(105)");
                adjustedValue(order, result);
                break;
            case 106:
                //;
                System.out.println("功能分类: 当前焊接程序名(106)");
                weldingName(order, result);
                break;
            case 107:
                //;
                System.out.println("功能分类: 当前登录TX的用户信息(107");
                userInformation(order, result);
                break;
            case 108:
                //;
                System.out.println("功能分类: 系统状态(108)");
                SysStatus(order, result);
                break;
            case 109:
                //;
                System.out.println("功能分类: 当前仪表索引值(109)");
                dashboardIndex(order, result);
                break;
            case 200:
                //;
                System.out.println("功能分类: 数据采集开始(200)");
                acquisitionStart(order, result);
                break;
            case 201:
                //;
                System.out.println("功能分类: 采集数据(201)");
                dataCollection(order, result);
                break;
            case 202:
                //;
                System.out.println("功能分类: 数据采集停止(202)");
                acquisitionStop(order, result);
                break;

            default:
                System.out.println("接收到的请求功能分类不存在！！！！");
                break;
        }


        return retrunMsg;
    }

    /**
     * 客户端请求登录(0)
     *
     * @param order
     */

 /*   private byte[] login(int order) {
        int mode = 0;
        short userNameLen;
        short passwordLen;
        String userName = "root";
        String password = "123456";
        //write and read
        byte[] mode_bytes = int2byte(mode);
        byte[] userName_byte = userName.getBytes();
        byte[] password_byte = password.getBytes();
        byte[] userNameLen_byte = shortToByteArray((short) userName.length());
        byte[] passwordLen_byte = shortToByteArray((short) password.length());
        byte[] content = new byte[mode_bytes.length + userName_byte.length + password_byte.length];
        return content;
        //String res = new String(mode_bytes,0,mode_bytes.length);
    }*/

    /**
     * 客户端请求登录应答(1)
     *
     * @param order
     */

    private void loginResponse(int order, byte[] result) {
        //目标数组4字节Result
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int Result = toInt(destBytes);
        System.out.println("(1)Result值为" + Result);
        //2字节字符串长度len+len个字节. 含义为Reason, 字符串长度+字符串utf8编码

        short i = twoByteAdd(destBytes[3], destBytes[2]);
        if (i >= 0) {
            byte[] destBytes2 = new byte[result.length];
            destBytes2 = changeBytes(destBytes2);
            System.arraycopy(result, 6, destBytes2, 0, i);
            String Reason = null;
            try {
                Reason = ToString(destBytes2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(1)Reason值为" + Reason);
        } else {
            System.out.println("(1)short值有误");
        }


    }

    /**
     * 客户端请求注销(2)
     *
     * @param order
     */

    private void logout(int order) {


    }

    /**
     * 服务器广播关闭(3)
     *
     * @param order
     */

    private void OffBroadcast(int order, byte[] result) {
        //目标数组4字节Result
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int Reason = toInt(destBytes);
        System.out.println("(3)Reason值为" + Reason);

    }

    /**
     * 系统标识(10)
     *
     * @param order
     */

    private void systemID(int order, byte[] result) {
        //2字节字符串长度len+len个字节. 含义为Reason, 字符串长度+字符串utf8编码
        short i = twoByteAdd(result[3], result[2]);
        if (i >= 0) {
            byte[] destBytes = new byte[i];
            System.arraycopy(result, 4, destBytes, 0, i);
            changeBytes(destBytes);
            String Reason = null;
            try {
                Reason = ToString(destBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(10)Reason值为" + Reason);
        } else {
            System.out.println("(10)short值有误");
        }
    }

    /**
     * 焊接状态通知(100)
     *
     * @param order
     */

    private void weldStatus(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[result.length];
        System.arraycopy(result, 2, destBytes, 0, 4);
        changeBytes(destBytes);
        int Status = toInt(destBytes);
        System.out.println("(3)Status值为" + Status);
        //2字节字符串长度len+len个字节. 含义为Text, 字符串长度+字符串utf8编码
        Short i = twoByteAdd(result[7], result[6]);
        if (i >= 0) {
            byte[] destBytes2 = new byte[i];
            System.arraycopy(result, 8, destBytes2, 0, i);
            destBytes2 = changeBytes(destBytes2);
            String Test = null;
            try {
                Test = ToString(destBytes2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(100)Test值为" + Test);

        } else {
            System.out.println("(100)short值有误");

        }
    }

    /**
     * 日志通知(101)
     *
     * @param order
     */

    private void logNotice(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[result.length];
        System.arraycopy(result, 2, destBytes, 0, 4);
        changeBytes(destBytes);
        int Level = toInt(destBytes);
        System.out.println("(101)Level值为" + Level);
        //2字节字符串长度len+len个字节. 含义为Message, 字符串长度+字符串utf8编码
        Short i = twoByteAdd(result[7], result[6]);
        if (i >= 0) {
            byte[] destBytes2 = new byte[i];
            System.arraycopy(result, 8, destBytes2, 0, i);
            destBytes2 = changeBytes(destBytes2);
            String Message = destBytes2.toString();
            System.out.println("(101)Message值为" + Message);
        } else {
            System.out.println("(101)short值有误");

        }


    }

    /**
     * 参数配置通知(102)
     *
     * @param order
     */

    private void parameter(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[result.length];
        System.arraycopy(result, 2, destBytes, 0, 4);
        changeBytes(destBytes);
        int Index = toInt(destBytes);
        System.out.println("(102)Index值为" + Index);

        //2字节字符串长度len+len个字节. 含义为DevName, 字符串长度+字符串utf8编码
        Short i = twoByteAdd(result[7], result[6]);
        byte[] destBytes2;
        if (i >= 0) {

            destBytes2 = new byte[i];
            System.arraycopy(result, 8, destBytes2, 0, i);
            destBytes2 = changeBytes(destBytes2);
            String DevName = null;
            try {
                DevName = ToString(destBytes2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(102)含义为DevName值为" + DevName);
        } else {
            System.out.println("(102)short值有误");
        }
        //2字节字符串长度len+len个字节. 含义为ParamName, 字符串长度+字符串utf8编码
        Short i2 = twoByteAdd(result[9 + i], result[8 + i]);
        byte[] destBytes3;
        if (i2 >= 0) {
            destBytes3 = new byte[i2];
            System.arraycopy(result, 10 + i, destBytes3, 0, i2);
            changeBytes(destBytes3);
            String ParamName = null;
            try {
                ParamName = ToString(destBytes3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(102)含义为ParamName值为" + ParamName);
        } else {
            System.out.println("(102)short值i2有误");
        }

        //2字节字符串长度len+len个字节. 含义为ParamUnit, 字符串长度+字符串utf8编码
        Short i3 = twoByteAdd(result[11 + i + i2], result[10 + i + i2]);
        if (i3 >= 0) {
            byte[] destByte4 = new byte[i3];
            System.arraycopy(result, 12 + i + i2, destByte4, 0, i3);
            destByte4 = changeBytes(destByte4);
            String ParamUnit = null;
            try {
                ParamUnit = ToString(destByte4);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(102)含义为ParamUnit值为" + ParamUnit);
        } else {
            System.out.println("(102)short值i3有误");
        }

        //4字节(quint32), 含义为Precision
        byte[] destBytes5 = new byte[result.length];
        System.arraycopy(result, 12 + i + i2 + i3, destBytes5, 0, 4);
        changeBytes(destBytes5);
        int Precision = toInt(destBytes);
        System.out.println("(102)Precision值为" + Precision);


    }

    /**
     * 反馈值通知(103)
     *
     * @param order
     */

    private void feedbackValue(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int Index = toInt(destBytes);
        System.out.println("(103)Index值为" + Index);

        //2字节字符串长度len+len个字节. 含义为DevName, 字符串长度+字符串utf8编码
        Short DispIndex = twoByteAdd(result[7], result[6]);
        System.out.println("(103)含义为DispIndex值为" + DispIndex);
        //2字节字符串长度len+len个字节. 含义为ParamName, 字符串长度+字符串utf8编码
        int Disabled = shortbyte2int(new byte[]{result[9], result[8]});
        System.out.println("(103)含义为Disabled值为" + Disabled);

        //4字节(double), 含义为Value
        byte[] destBytes4 = new byte[8];
        System.arraycopy(result, 10, destBytes4, 0, 8);
        destBytes4 = changeBytes(destBytes4);
        double Value = bytes2Double(destBytes4);
        System.out.println("(103)Value值为" + Value);

        //2字节(quint16), 含义为FillData
        int FillData = shortbyte2int(new byte[]{result[19], result[18]});
        System.out.println("(103)含义为FillData值为" + FillData);


    }

    /**
     * 编程值通知(104)
     *
     * @param order
     */

    private void programValue(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int Index = toInt(destBytes);
        System.out.println("(104)Index值为" + Index);

        //2字节字符串长度len+len个字节. 含义为DevName, 字符串长度+字符串utf8编码
        Short DispIndex = twoByteAdd(result[7], result[6]);
        System.out.println("(104)含义为DispIndex值为" + DispIndex);
        //2字节字符串长度len+len个字节. 含义为ParamName, 字符串长度+字符串utf8编码
        int Disabled = shortbyte2int(new byte[]{result[9], result[8]});
        System.out.println("(104)含义为Disabled值为" + Disabled);

        //4字节(double), 含义为Value
        byte[] destBytes4 = new byte[8];
        System.arraycopy(result, 10, destBytes4, 0, 8);
        destBytes4 = changeBytes(destBytes4);
        double Value = bytes2Double(destBytes4);
        System.out.println("(104)Value值为" + Value);

        //2字节(quint16), 含义为FillData
        int FillData = shortbyte2int(new byte[]{result[19], result[18]});
        System.out.println("(104)含义为FillData值为" + FillData);


    }

    /**
     * 调整值通知(105)
     *
     * @param order
     */

    private void adjustedValue(int order, byte[] result) {
        //4字节(qint32), 含义为Status
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int Index = toInt(destBytes);
        System.out.println("(105)Index值为" + Index);

        //2字节字符串长度len+len个字节. 含义为DevName, 字符串长度+字符串utf8编码
        Short DispIndex = twoByteAdd(result[7], result[6]);
        System.out.println("(105)含义为DispIndex值为" + DispIndex);
        //2字节字符串长度len+len个字节. 含义为ParamName, 字符串长度+字符串utf8编码
        int Disabled = shortbyte2int(new byte[]{result[9], result[8]});
        System.out.println("(105)含义为Disabled值为" + Disabled);

        //4字节(double), 含义为Value
        byte[] destBytes4 = new byte[8];
        System.arraycopy(result, 10, destBytes4, 0, 8);
        destBytes4 = changeBytes(destBytes4);
        double Value = bytes2Double(destBytes4);
        System.out.println("(105)Value值为" + Value);

        //2字节(quint16), 含义为FillData
        int FillData = shortbyte2int(new byte[]{result[19], result[18]});
        System.out.println("(105)含义为FillData值为" + FillData);


    }

    /**
     * 当前焊接程序名(106)
     *
     * @param order
     */

    private void weldingName(int order, byte[] result) {
        //2字节字符串长度len+len个字节. 含义为ProgName, 字符串长度+字符串utf8编码
        short i = twoByteAdd(result[3], result[2]);
        if (i >= 0) {
            byte[] destBytes = new byte[i];
            System.arraycopy(result, 4, destBytes, 0, i);
            changeBytes(destBytes);
            String ProgName = null;
            try {
                ProgName = ToString(destBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(106)ProgName值为" + ProgName);
        } else {
            System.out.println("(106)short值i有误");
        }


    }

    /**
     * 当前登录TX的用户信息(107)
     *
     * @param order
     */

    private void userInformation(int order, byte[] result) {
        //2字节字符串长度len+len个字节. UserName, 字符串长度+字符串utf8编码
        short i = twoByteAdd(result[3], result[2]);
        if (i >= 0) {
            byte[] destBytes = new byte[i];
            System.arraycopy(result, 4, destBytes, 0, i);
            changeBytes(destBytes);
            String UserName = null;
            try {
                UserName = ToString(destBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(107)UserName值为" + UserName);
        } else {
            System.out.println("(107)short值i有误");
        }
        //4字节(quint32), 含义为UserOption
        byte[] destBytes2 = new byte[4];
        System.arraycopy(result, 4 + i, destBytes2, 0, 4);
        destBytes2 = changeBytes(destBytes2);
        int UserOption = toInt(destBytes2);
        System.out.println("(107)UserOption值为" + UserOption);

        //2字节字符串长度len+len个字节. 含义为UserMemo, 字符串长度+字符串utf8编码
        short i2 = twoByteAdd(result[5 + i], result[4 + i]);
        if (i2 >= 0) {
            byte[] destBytes3 = new byte[i2];
            System.arraycopy(result, 6 + i, destBytes3, 0, i2);
            destBytes3 = changeBytes(destBytes3);
            String UserMemo = null;
            try {
                UserMemo = ToString(destBytes3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(107)UserMemo值为" + UserMemo);
        } else {
            System.out.println("(107)short值i2有误");
        }
    }

    /**
     * 系统状态(108)
     *
     * @param order
     */

    private void SysStatus(int order, byte[] result) {
        //4字节(qint32), SysStatus
        byte[] destBytes = new byte[4];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int SysStatus = toInt(destBytes);
        System.out.println("(108)SysStatus值为" + SysStatus);

    }

    /**
     * 当前仪表索引值(109)
     *
     * @param order
     */

    private void dashboardIndex(int order, byte[] result) {
        //2字节(quint16), 含义为MeterIdx
        short MeterIdx = twoByteAdd(result[3], result[2]);
        System.out.println("(109)MeterIdx值为" + MeterIdx);
    }

    /**
     * 数据采集开始(200)
     *
     * @param order
     */

    private void acquisitionStart(int order, byte[] result) {
        //4字节(qint32), AcqPeriod
        byte[] destBytes = new byte[result.length];
        System.arraycopy(result, 2, destBytes, 0, 4);
        destBytes = changeBytes(destBytes);
        int AcqPeriod = toInt(destBytes);
        System.out.println("(200)AcqPeriod值为" + AcqPeriod);
        //2字节字符串长度len+len个字节. SerialNo, 字符串长度+字符串utf8编码
        Short i = twoByteAdd(result[7], result[6]);
        if (i >= 0) {
            byte[] destBytes2 = new byte[i];
            System.arraycopy(result, 8, destBytes2, 0, i);
            destBytes2 = changeBytes(destBytes2);
            String SerialNo = null;
            try {
                SerialNo = ToString(destBytes2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(200)含义为SerialNo值为" + SerialNo);
        }else {
            System.out.println("(200)short值i有误");
        }
        //2字节字符串长度len+len个字节. MetaString, 字符串长度+字符串utf8编码
        Short i2 = twoByteAdd(result[9 + i], result[8 + i]);
        if (i2 >= 0) {
            byte[] destBytes3 = new byte[i2];
            System.arraycopy(result, 10 + i, destBytes3, 0, i2);
            destBytes3 = changeBytes(destBytes3);
            String MetaString = null;
            try {
                MetaString = ToString(destBytes3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(200)含义为MetaString值为" + MetaString);
        }else {
            System.out.println("(200)short值i2有误");
        }
        //2字节字符串长度len+len个字节. FileName, 字符串长度+字符串utf8编码
        Short i3 = twoByteAdd(result[11 + i + i2], result[10 + i + i2]);
        if (i3 >= 0) {
            byte[] destByte4 = new byte[i3];
            System.arraycopy(result, 12 + i + i2, destByte4, 0, i3);
            destByte4 = changeBytes(destByte4);
            String FileName = null;
            try {
                FileName = ToString(destByte4);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("(200)含义为FileName值为" + FileName);
        }else {
            System.out.println("(200)short值i3有误");
        }

        //2字节列表长度len+len*4字节 含义为ParamIndexs(长度n+长度n个qint32数据)
        short i4=twoByteAdd(result[3], result[2]);
        List<byte[]> list = new ArrayList<byte[]>();
        if (i4 >= 0) {
            for (int j = 0; j < i4; j++) {
                byte[] bytes = new byte[4];
                System.arraycopy(result, 4 + j * 4, bytes, 0, 4);
                bytes = changeBytes(bytes);
                int ParamIndexs = toInt(bytes);
                System.out.println("(200)含义为ParamIndexs第" + j + "值" + ParamIndexs);

            }
        } else {
            System.out.println("(200)ParamIndexs值有误" );
        }
        System.out.println(list);


    }

    /**
     * 采集数据(201)
     *
     * @param order
     */

    private void dataCollection(int order, byte[] result) {
        //2字节列表长度len+len*4个字节，含义为Values(长度+长度个double数据)
        short i4=twoByteAdd(result[3], result[2]);
        List<byte[]> list = new ArrayList<byte[]>();
        if (i4 >= 0) {
            for (int j = 0; j < i4; j++) {
                byte[] bytes = new byte[4];
                System.arraycopy(result, 4 + j * 4, bytes, 0, 4);
                bytes = changeBytes(bytes);
                //double num = BitConverter.ToDouble(bytes, 0);
                //System.out.println("(200)含义为ParamIndexs第" + j + "值" + ParamIndexs);

            }
        } else {
            System.out.println("(200)ParamIndexs值有误" );
        }
        System.out.println(list);


    }

    /**
     * 数据采集停止(202)
     *
     * @param order
     */

    private void acquisitionStop(int order, byte[] result) {
        //无内容
        // 保留命令
    }


}

