package utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class change_util {
    /**
     * 将16位的short转换成byte数组
     *
     * @param s short
     * @return byte[] 长度为2
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }


    /**
     * 32位int转byte[]
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * byte转化为int
     *
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
        int number = 0;
        for (int i = 0; i < 4; i++) {
            number += bytes[i] << i * 8;
        }
        return number;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        /*String str = "84846451dfghjkl";
        System.out.println(str.substring(8));
        byte[] content = str.getBytes();*/
        /*String str = "wo";
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        System.out.println(length);*/

        byte[] bytes = new byte[]{31, 1, 1, 0};
        byte[] bytes2 = new byte[]{-36,63};
//
//        final ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
//
//        buffer.put(bytes, 0, buffer.limit());
//
//        byte[] result = buffer.array();
//        String a="adfdsfa";
//        byte[] bytes1 = a.getBytes();
//
//
//        System.out.println(new String(result,"utf-8"));
//        System.out.println(new String(bytes1,"utf-8"));
//        System.out.println(result);
        short i = twoByteAdd(bytes[1], bytes[0]);
        int y=toInt(bytes);
        System.out.println(y);
        byte[] destBytes2 = new byte[0];
        System.out.println(Arrays.toString(destBytes2));
    }

    public static String  ToString (byte[] result) throws UnsupportedEncodingException {
       return new String(result,"UTF-8");
    }

    //将两个
    public static short twoByteAdd(byte result1, byte result2) {
        short s = 0;   //一个16位整形变量，初值为 0000 0000 0000 0000
        byte b1 = result1;   //一个byte的变量，作为转换后的高8位
        byte b2 = result2;   //一个byte的变量，作为转换后的低8位
        s = (short) (s ^ b1);  //将b1赋给s的低8位
        s = (short) (s << 8);  //s的低8位移动到高8位
        s = (short) (s ^ b2); //在b2赋给s的低8位
        return s;
    }

    public static short[] byteArray2ShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);

        return retVal;
    }
    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    public static void arrayCopy(){

    //源数组
    byte[] srcBytes = new byte[]{2, 4, 0, 0, 0, 0, 0, 10, 15, 50};
    //目标数组
    byte[] destBytes = new byte[srcBytes.length];

    //我们使用System.arraycopy进行转换(copy)

            System.arraycopy(srcBytes,0,destBytes,0,5);
}
    /**
     * 切换大小端续
     */
    public static byte[] changeBytes(byte[] a){
        byte[] b = new byte[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[b.length - i - 1];
        }
        return b;
    }

    // 读取2个字节转为无符号整型
    public static int shortbyte2int(byte[] res) {
        DataInputStream dataInputStream = new DataInputStream(
                new ByteArrayInputStream(res));
        int a = 0;
        try {
            a = dataInputStream.readUnsignedShort();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return a;
    }



}
