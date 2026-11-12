package io.github.wycst.wast.json;

import io.github.wycst.wast.json.exceptions.JSONException;
import io.github.wycst.wast.json.options.ReadOption;
import io.github.wycst.wast.json.options.ReadOptionConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * json解析上下文配置
 *
 * @Author: wangy
 * @Date: 2021/12/19 14:47
 * @Description:
 */
public class JSONParseContext {

    final ReadOptionConfig optionConfig;

    JSONParseContext() {
        this(ReadOptionConfig.DEFAULT);
    }

    JSONParseContext(ReadOptionConfig optionConfig) {
        this.optionConfig = optionConfig;
    }

    JSONParseContext(ReadOptionConfig optionConfig, String input) {
        this.optionConfig = optionConfig;
        this.input = input;
    }

    JSONParseContext(ReadOption[] readOptions) {
        this(ReadOptionConfig.of(readOptions));
    }

    String input;
    public int endIndex;
    int depth;

    public final static int MAX_NEST_DEPTH = 1024;

    JSONCharArrayWriter writer;

    boolean escape = true;
    int escapeOffset = -1;

    // 开启校验模式（调用validate方法时）
    boolean validate;
    boolean validateFail;

    // note: initialize an empty array to be cleared to null in reset, retaining only the slot positions
    private String[] strings;
    final static ThreadLocal<String[]> STRINGS_TL = new ThreadLocal<String[]>() {
        @Override
        protected String[] initialValue() {
            return new String[32];
        }
    };

    static JSONParseContext of(ReadOption[] readOptions) {
        return new JSONParseContext(ReadOptionConfig.of(readOptions));
    }

    static JSONParseContext of(ReadOption[] readOptions, String input) {
        return new JSONParseContext(ReadOptionConfig.of(readOptions), input);
    }

    protected final JSONParseContext input(String input) {
        this.input = input;
        return this;
    }

    protected void setElementSize(int elementSize) {
    }

    void setContextWriter(JSONCharArrayWriter writer) {
        this.writer = writer;
    }

    JSONCharArrayWriter getContextWriter() {
        if (writer != null) {
            writer.clear();
        }
        return writer;
    }

    String[] getContextStrings() {
        return strings = STRINGS_TL.get();
    }

    void clear() {
        depth = 0;
        if (writer != null) {
            writer.reset();
            writer = null;
        }
        if(strings != null) {
            Arrays.fill(strings, null);
            strings = null;
        }
        input = null;
    }

    final boolean checkEscapeBackslashJDK16(String input, int fromIndex, int endIndex) {
        if (!escape || endIndex < escapeOffset) return false;
        if (fromIndex > escapeOffset) {
            escapeOffset = input.indexOf('\\', fromIndex);
            escape = escapeOffset > -1;
            if (!escape) return false;
        }
        return endIndex > escapeOffset;
    }

    final int getEscapeOffset() {
        return escapeOffset;
    }

    JSONKeyValueMap<String> getTable32() {
        return JSONGeneral.KEY_32_TABLE;
    }

    JSONKeyValueMap<String> getTable8() {
        return JSONGeneral.KEY_8_TABLE;
    }

    protected final String getCacheEightCharsKey(char[] buf, int offset, int len, long hashValue) {
        return JSONGeneral.getCacheEightCharsKey(buf, offset, len, hashValue, getTable8());
    }

    protected final String getCacheKey(char[] buf, int offset, int len, long hashValue) {
        return JSONGeneral.getCacheKey(buf, offset, len, hashValue, getTable32());
    }

    protected final String getCacheEightBytesKey(byte[] bytes, int offset, int len, long hashValue) {
        return JSONGeneral.getCacheEightBytesKey(bytes, offset, len, hashValue, getTable8());
    }

    protected final String getCacheKey(byte[] bytes, int offset, int len, long hashValue) {
        return JSONGeneral.getCacheKey(bytes, offset, len, hashValue, getTable32());
    }

    public final JSONParseContext enter() {
        if (++depth > MAX_NEST_DEPTH) {
            throw new JSONException("Nested too deeply, depth " + depth);
        }
        return this;
    }

    public final JSONParseContext leave() {
        --depth;
        return this;
    }

    public Map<Serializable, Object> defaultMap() {
        return new JSONArrayMap<Serializable, Object>(10);
    }

    public List<?> defaultList() {
        return new ArrayList<Object>(10);
    }
}
