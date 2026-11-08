package io.github.wycst.wast.json;

final class UTF16ByteArraySource implements CharSource {

    @Override
    public String substring(String input, byte[] bytes, int beginIndex, int endIndex) {
        return input.substring(beginIndex, endIndex);
    }

    @Override
    public void writeString(JSONCharArrayWriter writer, String input, byte[] buf, int offset, int len) {
        writer.writeString(input, offset, len);
    }
}
