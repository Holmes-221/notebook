
/*
异常编码检查
 */
package test01;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
public class error {
    private static String[] charsetArr = {"UTF-8","GB18030","GB2312","GBK","Windows-1252","ISO8859-1"};

    public static void testAllCharset(String text) throws UnsupportedEncodingException {
        if (text == null || text.length() == 0) {
            System.out.println("文本不能为空");
            return;
        }

        System.out.println("假设当前编码       假设原始编码          编码后的内容");
        printSeparator();

        for (String curCharset : charsetArr) {
            byte[] btArr = text.getBytes(curCharset);
            for (String originCharset : charsetArr) {
                if (originCharset.equals(curCharset)) {
                    continue;
                }
                String encodeText = new String(btArr,originCharset);
                printTable(curCharset, originCharset, encodeText);
            }
            printSeparator();
        }
    }

    private static void printSeparator() {
        System.out.println("--------------------------------------------------------");
    }

    private static void printTable(String curCharset, String originCharset, String encodeText) {
        System.out.print(curCharset);
        for (int i = 0; i < 15 - curCharset.length(); i++) {
            System.out.print(" ");
        }
        System.out.print("|   " + originCharset);
        for (int i = 0; i < 16 - originCharset.length(); i++) {
            System.out.print(" ");
        }
        System.out.println("|     " + encodeText);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //测试乱码
        Scanner s = new Scanner(System.in);
        String code = s.next();
        testAllCharset(code);//https://client.vpn.nuist.edu.cn/餴/webvpn62756c6c6574696e2e6e756973742e6564752e636e/2021/0511/c791a176495/page.psp
    }
}
//https://client.vpn.nuist.edu.cn/餴/webvpn62756c6c6574696e2e6e756973742e6564752e636e/2021/0511/c791a176495/page.psp