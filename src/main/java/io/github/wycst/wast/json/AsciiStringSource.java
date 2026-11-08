package io.github.wycst.wast.json;

final class AsciiStringSource implements CharSource {

    public AsciiStringSource() {
    }

    @Override
    public String substring(String input, byte[] bytes, int beginIndex, int endIndex) {
        return JSONMemoryHandle.createAsciiString(bytes, beginIndex, endIndex - beginIndex);
    }

    @Override
    public void writeString(JSONCharArrayWriter writer, String input, byte[] buf, int offset, int len) {
        writer.writeString(input, offset, len);
    }


}
