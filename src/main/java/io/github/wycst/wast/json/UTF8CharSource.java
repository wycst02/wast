package io.github.wycst.wast.json;

import io.github.wycst.wast.common.utils.EnvUtils;

final class UTF8CharSource implements CharSource {

    @Override
    public String substring(String input, byte[] bytes, int beginIndex, int endIndex) {
        return new String(bytes, beginIndex, endIndex - beginIndex, EnvUtils.CHARSET_UTF_8);
    }

    @Override
    public void writeString(JSONCharArrayWriter writer, String input, byte[] buf, int offset, int len) {
        writer.writeBytes(buf, offset, len);
    }
}
