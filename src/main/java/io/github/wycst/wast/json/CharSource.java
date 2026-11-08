package io.github.wycst.wast.json;

/**
 * 不同JDK环境下字符串结构抽象处理
 *
 * @Author: wangy
 * @Date: 2022/8/21 19:26
 * @Description:
 */
public interface CharSource {
    CharSource ASCII = new AsciiStringSource();
    CharSource UTF8 = new UTF8CharSource();
    CharSource UTF16 = new UTF16ByteArraySource();

    String substring(String input, byte[] bytes, int beginIndex, int endIndex);

    void writeString(JSONCharArrayWriter writer, String input, byte[] buf, int offset, int len);
}
