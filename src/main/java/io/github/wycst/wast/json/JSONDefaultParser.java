/*
 * Copyright [2020-2026] [wangyunchao]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.wycst.wast.json;

import io.github.wycst.wast.common.reflect.GenericParameterizedType;
import io.github.wycst.wast.common.utils.EnvUtils;
import io.github.wycst.wast.json.exceptions.JSONException;
import io.github.wycst.wast.json.options.ReadOption;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * <p> 默认解析器，根据字符的开头前缀解析为Map、List、String
 * <p> 去掉类型check问题，理论上最快。
 *
 * @Author wangyunchao
 * @Date 2022/5/31 21:28
 */
@SuppressWarnings({"all"})
final class JSONDefaultParser {

    JSONTypeDeserializer.CharSequenceImpl stringDeserializer;
    JSONTypeDeserializer.NumberImpl numberDeserializer;

    JSONDefaultParser() {
        this.stringDeserializer = JSONTypeDeserializer.CHAR_SEQUENCE_STRING;
        this.numberDeserializer = JSONTypeDeserializer.NUMBER;
    }

    void setStringDeserializer(JSONTypeDeserializer.CharSequenceImpl stringDeserializer) {
        this.stringDeserializer = stringDeserializer;
    }

    public void setNumberDeserializer(JSONTypeDeserializer.NumberImpl numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    /**
     * return Map or List
     *
     * @param json
     * @param readOptions
     * @return Map or List
     */
    public Object parse(String json, ReadOption... readOptions) {
        json.getClass();
        if (EnvUtils.JDK_9_PLUS) {
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == json.length()) {
                return parseInternal(CharSource.ASCII, bytes, 0, bytes.length, null, JSONParseContext.of(readOptions, json));
            } else {
                char[] chars = json.toCharArray();
                return parseInternal(CharSource.UTF16, chars, 0, chars.length, null, JSONParseContext.of(readOptions, json));
            }
        }
        char[] chars = (char[]) JSONMemoryHandle.getStringValue(json);
        return parseInternal(null, chars, 0, chars.length, null, JSONParseContext.of(readOptions));
    }

    /**
     * json -> Map or List or String or Number or null
     *
     * @param json
     * @param parseContext
     * @return Map or List
     */
    Object parse(String json, JSONParseContext parseContext) {
        json.getClass();
        if (EnvUtils.JDK_9_PLUS) {
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == json.length()) {
                return parseInternal(CharSource.ASCII, bytes, 0, bytes.length, null, parseContext.input(json));
            } else {
                char[] chars = json.toCharArray();
                return parseInternal(CharSource.UTF16, chars, 0, chars.length, null, parseContext.input(json));
            }
        }
        char[] chars = (char[]) JSONMemoryHandle.getStringValue(json);
        return parseInternal(null, chars, 0, chars.length, null, parseContext);
    }

    /**
     * 解析buf返回Map或者List
     *
     * @param buf
     * @param readOptions
     * @return
     */
    public Object parse(char[] buf, ReadOption... readOptions) {
        if (EnvUtils.JDK_9_PLUS) {
            String json = new String(buf);
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == buf.length) {
                return parseInternal(CharSource.ASCII, bytes, 0, bytes.length, null, JSONParseContext.of(readOptions, json));
            } else {
                return parseInternal(CharSource.UTF16, buf, 0, buf.length, null, JSONParseContext.of(readOptions, json));
            }
        }
        return parseInternal(null, buf, 0, buf.length, null, JSONParseContext.of(readOptions));
    }

    /**
     * 解析buf返回Map或者List(JSONReader专用)
     *
     * @param buf
     * @param offset
     * @param len
     * @param readOptions
     * @return
     */
    Object parse(char[] buf, int offset, int len, ReadOption... readOptions) {
        if (EnvUtils.JDK_9_PLUS) {
            String json = new String(buf, offset, len);
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == json.length()) {
                return parseInternal(CharSource.ASCII, bytes, 0, bytes.length, null, JSONParseContext.of(readOptions, json));
            } else {
                char[] chars = len == buf.length ? buf : json.toCharArray();
                return parseInternal(CharSource.UTF16, chars, 0, chars.length, null, JSONParseContext.of(readOptions, json));
            }
        }
        return parseInternal(null, buf, offset, len, null, JSONParseContext.of(readOptions));
    }

    /**
     * 指定外层的map类型
     *
     * @param json
     * @param mapCls
     * @param readOptions
     * @return
     */
    Map parseMap(String json, Class<? extends Map> mapCls, ReadOption... readOptions) {
        json.getClass();
        if (EnvUtils.JDK_9_PLUS) {
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == json.length()) {
                return (Map) parseInternal(CharSource.ASCII, bytes, 0, bytes.length, JSONGeneral.createMapInstance(mapCls), JSONParseContext.of(readOptions, json));
            } else {
                char[] chars = json.toCharArray();
                return (Map) parseInternal(CharSource.UTF16, chars, 0, chars.length, JSONGeneral.createMapInstance(mapCls), JSONParseContext.of(readOptions, json));
            }
        }
        char[] chars = (char[]) JSONMemoryHandle.getStringValue(json);
        return (Map) parseInternal(null, chars, 0, chars.length, JSONGeneral.createMapInstance(mapCls), JSONParseContext.of(readOptions));
    }

    /**
     * 指定外层的map类型
     *
     * @param json
     * @param listCls
     * @param readOptions
     * @return
     */
    Collection parseCollection(String json, Class<? extends Collection> listCls, ReadOption... readOptions) {
        json.getClass();
        if (EnvUtils.JDK_9_PLUS) {
            byte[] bytes = (byte[]) JSONMemoryHandle.getStringValue(json);
            if (bytes.length == json.length()) {
                return (Collection) parseInternal(CharSource.ASCII, bytes, 0, bytes.length, JSONGeneral.createCollectionInstance(listCls), JSONParseContext.of(readOptions, json));
            } else {
                char[] chars = json.toCharArray();
                return (Collection) parseInternal(CharSource.UTF16, chars, 0, chars.length, JSONGeneral.createCollectionInstance(listCls), JSONParseContext.of(readOptions, json));
            }
        }
        char[] chars = (char[]) JSONMemoryHandle.getStringValue(json);
        return (Collection) parseInternal(null, chars, 0, chars.length, JSONGeneral.createCollectionInstance(listCls), JSONParseContext.of(readOptions));
    }

    Object parseInternal(CharSource source, char[] buf, int fromIndex, int toIndex, Object defaultValue, JSONParseContext parseContext) {
        char beginChar;
        try {
            while ((beginChar = buf[fromIndex]) <= ' ') {
                ++fromIndex;
            }
            while (buf[toIndex - 1] <= ' ') {
                --toIndex;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Syntax error, not supported empty source");
        }
        try {
            boolean allowComment = parseContext.optionConfig.allowComment;
            if (allowComment && beginChar == '/') {
                fromIndex = JSONGeneral.clearCommentAndWhiteSpaces(buf, fromIndex + 1, parseContext);
                beginChar = buf[fromIndex];
            }
            Object result;
            switch (beginChar) {
                case '{':
                    result = parseJSONObject(source, buf, fromIndex, defaultValue == null ? parseContext.defaultMap() : (Map) defaultValue, parseContext);
                    break;
                case '[':
                    result = parseJSONArray(source, buf, fromIndex, defaultValue == null ? parseContext.defaultList() : (Collection) defaultValue, parseContext);
                    break;
                case '\'':
                case '"':
                    result = stringDeserializer.deserializeString(source, buf, fromIndex, beginChar, null, parseContext);
                    break;
                default:
                    try {
                        switch (beginChar) {
                            case 't': {
                                result = JSONTypeDeserializer.parseTrue(buf, fromIndex, parseContext);
                                break;
                            }
                            case 'f': {
                                result = JSONTypeDeserializer.parseFalse(buf, fromIndex, parseContext);
                                break;
                            }
                            case 'n': {
                                result = JSONTypeDeserializer.parseNull(buf, fromIndex, parseContext);
                                break;
                            }
                            default: {
                                char[] numBuf = Arrays.copyOfRange(buf, fromIndex, toIndex + 1);
                                numBuf[numBuf.length - 1] = ',';
                                toIndex = numBuf.length - 1;
                                result = JSONTypeDeserializer.NUMBER.deserialize(source, numBuf, 0, parseContext.optionConfig.useBigDecimalAsDefault ? GenericParameterizedType.BigDecimalType : GenericParameterizedType.AnyType, null, ',', parseContext);
                            }
                        }
                    } catch (Throwable throwable) {
                        throw throwable instanceof JSONException ? (JSONException) throwable : new UnsupportedOperationException("Unsupported for begin character with '" + beginChar + "'");
                    }
            }

            int endIndex = parseContext.endIndex;

            if (allowComment) {
                /** Remove comments at the end of the declaration */
                if (endIndex < toIndex - 1) {
                    char commentStart = '\0';
                    while (endIndex + 1 < toIndex && (commentStart = buf[++endIndex]) <= ' ') ;
                    if (commentStart == '/') {
                        endIndex = JSONGeneral.clearCommentAndWhiteSpaces(buf, endIndex + 1, parseContext);
                    }
                }
            }
            if (endIndex != toIndex - 1) {
                int wordNum = Math.min(50, buf.length - endIndex - 1);
                String errorContextTextAt = JSONGeneral.createErrorContextText(buf, endIndex + 1);
                throw new JSONException("Syntax error, at pos " + endIndex + ", context text by '" + errorContextTextAt + "', extra characters found, '" + new String(buf, endIndex + 1, wordNum) + " ...'");
            }
            return result;
        } catch (Exception ex) {
            JSONGeneral.handleCatchException(ex, buf, toIndex);
            throw new JSONException("Error: " + ex.getMessage(), ex);
        } finally {
            parseContext.clear();
        }
    }

    Collection parseJSONArray(CharSource source, char[] buf, int fromIndex, Collection list, JSONParseContext parseContext) throws Exception {
        final boolean allowComment = parseContext.optionConfig.allowComment, allowLastEndComma = parseContext.optionConfig.allowLastEndComma;
        int i = JSONTypeDeserializer.skipWhiteSpacesOrComment(buf, fromIndex, allowComment, parseContext);
        char ch = buf[i];
        if (ch == ']') {
            parseContext.endIndex = i;
            return list;
        }

        boolean isComma;
        for (; ; ) {
            list.add(readValue(source, buf, i, ch, ']', parseContext));
            ch = buf[i = JSONTypeDeserializer.skipWhiteSpacesOrComment(buf, parseContext.endIndex, allowComment, parseContext)];
            if (ch == ']' || ((isComma = ch == ',') && ((ch = buf[i = JSONTypeDeserializer.skipWhiteSpacesOrComment(buf, i, allowComment, parseContext)]) == ']') && allowLastEndComma)) {
                parseContext.endIndex = i;
                return list;
            }
            if (!isComma) {
                String errorContextTextAt = JSONGeneral.createErrorContextText(buf, i);
                throw new JSONException("Syntax error, at pos " + i + ", context text by '" + errorContextTextAt + "', unexpected '" + ch + "', expected ',' or ']'");
            }
        }
    }

    Map parseJSONObject(CharSource source, char[] buf, int fromIndex, Map instance, JSONParseContext parseContext) throws Exception {
        int i = fromIndex;
        char ch;
        boolean empty = true;
        final boolean allowomment = parseContext.optionConfig.allowComment, disableCacheMapKey = parseContext.optionConfig.disableCacheMapKey;
        for (; ; ) {
            while ((ch = buf[++i]) <= ' ') ;
            if (allowomment) {
                if (ch == '/') {
                    ch = buf[i = JSONGeneral.clearCommentAndWhiteSpaces(buf, i + 1, parseContext)];
                }
            }
            Serializable key;
            int fieldKeyFrom = i;
            if (ch == '"') {
                key = disableCacheMapKey ? (String) stringDeserializer.deserializeString(source, buf, i, '"', null, parseContext) : parseMapKeyByCache(buf, i, '"', parseContext);
                i = parseContext.endIndex + 1;
                empty = false;
            } else {
                if (ch == '}') {
                    if (!empty && !parseContext.optionConfig.allowLastEndComma) {
                        throw new JSONException("Syntax error, at pos " + i + ", the closing symbol '}' is not allowed here.");
                    }
                    parseContext.endIndex = i;
                    return instance;
                }
                if (ch == '\'') {
                    if (parseContext.optionConfig.allowSingleQuotes) {
                        while ((buf[++i] != '\'' || buf[i - 1] == '\\')) ;
                        empty = false;
                        ++i;
                        key = JSONGeneral.parseKeyOfMap(buf, fieldKeyFrom, i, false);
                    } else {
                        throw new JSONException("Syntax error, at pos " + i + ", the single quote symbol ' is not allowed here.");
                    }
                } else {
                    if (parseContext.optionConfig.allowUnquotedFieldNames) {
                        while (buf[++i] != ':') ;
                        empty = false;
                        key = JSONGeneral.parseKeyOfMap(buf, fieldKeyFrom, i, true);
                    } else {
                        int j = i;
                        boolean isNullKey = false;
                        key = null;
                        if (ch == 'n' && buf[++i] == 'u' && buf[++i] == 'l' && buf[++i] == 'l') {
                            isNullKey = true;
                            ++i;
                        }
                        if (!isNullKey) {
                            String errorContextTextAt = JSONGeneral.createErrorContextText(buf, j);
                            throw new JSONException("Syntax error, at pos " + j + ", context text by '" + errorContextTextAt + "', unexpected '" + ch + "', expected '\"' or use option ReadOption.AllowUnquotedFieldNames ");
                        }
                    }
                }
            }
            while ((ch = buf[i]) <= ' ') {
                ++i;
            }
            if (allowomment) {
                if (ch == '/') {
                    ch = buf[i = JSONGeneral.clearCommentAndWhiteSpaces(buf, i + 1, parseContext)];
                }
            }
            if (ch == ':') {
                while ((ch = buf[++i]) <= ' ') ;
                if (allowomment) {
                    if (ch == '/') {
                        ch = buf[i = JSONGeneral.clearCommentAndWhiteSpaces(buf, i + 1, parseContext)];
                    }
                }
                Object value = readValue(source, buf, i, ch, '}', parseContext);
                i = parseContext.endIndex;
                instance.put(key, value);
                while ((ch = buf[++i]) <= ' ') ;
                if (allowomment) {
                    if (ch == '/') {
                        ch = buf[i = JSONGeneral.clearCommentAndWhiteSpaces(buf, i + 1, parseContext)];
                    }
                }
                if (ch == ',') {
                    continue;
                }
                if (ch == '}') {
                    parseContext.endIndex = i;
                    return instance;
                }
                String errorContextTextAt = JSONGeneral.createErrorContextText(buf, i);
                throw new JSONException("Syntax error, at pos " + i + ", context text by '" + errorContextTextAt + "', unexpected '" + ch + "', expected ',' or '}'");
            } else {
                throw new JSONException("Syntax error, at pos " + i + ", unexpected '" + ch + "', token ':' is expected.");
            }
        }
    }

    static String parseMapKeyByCache(char[] buf, int from, char endCh, JSONParseContext jsonParseContext) {
        int beginIndex = from + 1, i = beginIndex;
        char ch;
        int len;
        JSONCharArrayWriter writer = null;
        boolean escape = false;
        boolean ascii = true;
        for (; ; ) {
            long hashValue = JSONGeneral.ESCAPE_BACKSLASH;
            while ((ch = buf[i]) != '\\' && ch != endCh) {
                ++i;
                if (ch > 0xFF) {
                    hashValue = (hashValue << 16) | ch;
                    ascii = false;
                } else {
                    hashValue = hashValue << 8 | ch;
                }
            }
            if (ch == '\\') {
                if (writer == null) {
                    writer = JSONGeneral.getContextWriter(jsonParseContext);
                }
                escape = true;
                if (i > beginIndex) {
                    writer.write(buf, beginIndex, i - beginIndex);
                }
                i = beginIndex = JSONGeneral.escapeNextChars(buf, buf[i + 1], i, writer);
            } else {
                jsonParseContext.endIndex = i;
                len = i - beginIndex;
                if (escape) {
                    writer.write(buf, beginIndex, len);
                    return writer.toString();
                } else {
                    if (ascii && len <= 8) {
                        return jsonParseContext.getCacheEightCharsKey(buf, beginIndex, len, hashValue);
                    }
                    return jsonParseContext.getCacheKey(buf, beginIndex, len, hashValue);
                }
            }
        }
    }

    // bytes #####################################################################################################
    Object parseInternal(CharSource source, byte[] bytes, int fromIndex, int toIndex, Object defaultValue, JSONParseContext parseContext) {
        byte beginByte;
        try {
            while ((beginByte = bytes[fromIndex]) <= ' ') {
                ++fromIndex;
            }
            while (bytes[toIndex - 1] <= ' ') {
                --toIndex;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Syntax error, not supported empty source");
        }
        try {
            boolean allowComment = parseContext.optionConfig.allowComment;
            if (allowComment && beginByte == '/') {
                fromIndex = JSONGeneral.clearCommentAndWhiteSpaces(bytes, fromIndex + 1, parseContext);
                beginByte = bytes[fromIndex];
            }
            Object result;
            switch (beginByte) {
                case '{':
                    result = parseJSONObject(source, bytes, fromIndex, defaultValue == null ? parseContext.defaultMap() : (Map) defaultValue, parseContext);
                    break;
                case '[':
                    result = parseJSONArray(source, bytes, fromIndex, defaultValue == null ? parseContext.defaultList() : (Collection) defaultValue, parseContext);
                    break;
                case '\'':
                case '"':
                    result = stringDeserializer.deserializeString(source, bytes, fromIndex, beginByte, GenericParameterizedType.StringType, parseContext);
                    break;
                default:
                    try {
                        switch (beginByte) {
                            case 't': {
                                result = JSONTypeDeserializer.parseTrue(bytes, fromIndex, parseContext);
                                break;
                            }
                            case 'f': {
                                result = JSONTypeDeserializer.parseFalse(bytes, fromIndex, parseContext);
                                break;
                            }
                            case 'n': {
                                result = JSONTypeDeserializer.parseNull(bytes, fromIndex, parseContext);
                                break;
                            }
                            default: {
                                byte[] numBuf = Arrays.copyOfRange(bytes, fromIndex, toIndex + 1);
                                numBuf[numBuf.length - 1] = ',';
                                result = JSONTypeDeserializer.NUMBER.deserialize(source, numBuf, 0, parseContext.optionConfig.useBigDecimalAsDefault ? GenericParameterizedType.BigDecimalType : GenericParameterizedType.AnyType, null, (byte) ',', parseContext);
                                toIndex = numBuf.length - 1;
                            }
                        }
                    } catch (Throwable throwable) {
                        throw throwable instanceof JSONException ? (JSONException) throwable : new UnsupportedOperationException("Unsupported for begin character with '" + (char) beginByte + "'");
                    }
            }

            int endIndex = parseContext.endIndex;

            if (allowComment) {
                if (endIndex < toIndex - 1) {
                    char commentStart = '\0';
                    while (endIndex + 1 < toIndex && (commentStart = (char) bytes[++endIndex]) <= ' ') ;
                    if (commentStart == '/') {
                        endIndex = JSONGeneral.clearCommentAndWhiteSpaces(bytes, endIndex + 1, parseContext);
                    }
                }
            }
            if (endIndex != toIndex - 1) {
                int wordNum = Math.min(50, bytes.length - endIndex - 1);
                String errorContextTextAt = JSONGeneral.createErrorContextText(bytes, endIndex + 1);
                throw new JSONException("Syntax error, at pos " + endIndex + ", context text by '" + errorContextTextAt + "', extra characters found, '" + new String(bytes, endIndex + 1, wordNum) + " ...'");
            }
            return result;
        } catch (Exception ex) {
            JSONGeneral.handleCatchException(ex, bytes, toIndex);
            throw new JSONException("Error: " + ex.getMessage(), ex);
        } finally {
            parseContext.clear();
        }
    }

    // 解析JSON值(byte版本)，返回解析后的value，调用方需用 parseContext.endIndex 更新游标
    private Object readValue(CharSource source, byte[] bytes, int i, byte b, byte endChar, JSONParseContext parseContext) throws Exception {
        switch (b) {
            case '{': {
                return parseJSONObject(source, bytes, i, parseContext.defaultMap(), parseContext);
            }
            case '[': {
                return parseJSONArray(source, bytes, i, parseContext.defaultList(), parseContext);
            }
            case '\'':
            case '"': {
                return stringDeserializer.deserializeString(source, bytes, i, b, GenericParameterizedType.StringType, parseContext);
            }
            case 'n':
                return JSONTypeDeserializer.parseNull(bytes, i, parseContext);
            case 't':
                return JSONTypeDeserializer.parseTrue(bytes, i, parseContext);
            case 'f':
                return JSONTypeDeserializer.parseFalse(bytes, i, parseContext);
            default: {
                return JSONTypeDeserializer.NUMBER.deserialize(source, bytes, i,
                        parseContext.optionConfig.useBigDecimalAsDefault ? GenericParameterizedType.BigDecimalType : GenericParameterizedType.AnyType,
                        null, endChar, parseContext);
            }
        }
    }

    // 解析JSON值(char版本)，返回解析后的value，调用方需用 parseContext.endIndex 更新游标
    private Object readValue(CharSource source, char[] buf, int i, char ch, char endChar, JSONParseContext parseContext) throws Exception {
        switch (ch) {
            case '{': {
                return parseJSONObject(source, buf, i, parseContext.defaultMap(), parseContext);
            }
            case '[': {
                return parseJSONArray(source, buf, i, parseContext.defaultList(), parseContext);
            }
            case '\'':
            case '"': {
                return stringDeserializer.deserializeString(source, buf, i, ch, null, parseContext);
            }
            case 'n':
                return JSONTypeDeserializer.parseNull(buf, i, parseContext);
            case 't':
                return JSONTypeDeserializer.parseTrue(buf, i, parseContext);
            case 'f':
                return JSONTypeDeserializer.parseFalse(buf, i, parseContext);
            default: {
                return JSONTypeDeserializer.NUMBER.deserialize(source, buf, i,
                        parseContext.optionConfig.useBigDecimalAsDefault ? GenericParameterizedType.BigDecimalType : GenericParameterizedType.AnyType,
                        null, endChar, parseContext);
            }
        }
    }

    Collection parseJSONArray(CharSource source, byte[] bytes, int fromIndex, Collection list, JSONParseContext parseContext) throws Exception {
        final boolean allowComment = parseContext.optionConfig.allowComment, allowLastEndComma = parseContext.optionConfig.allowLastEndComma;
        int i = JSONTypeDeserializer.skipWhiteSpacesOrComment(bytes, fromIndex, allowComment, parseContext);
        byte b = bytes[i];
        if (b == ']') {
            parseContext.endIndex = i;
            return list;
        }

        boolean isComma;
        for (; ; ) {
            list.add(readValue(source, bytes, i, b, JSONGeneral.END_ARRAY, parseContext));
            b = bytes[i = JSONTypeDeserializer.skipWhiteSpacesOrComment(bytes, parseContext.endIndex, allowComment, parseContext)];
            if (b == JSONGeneral.END_ARRAY || ((isComma = b == JSONGeneral.COMMA) && ((b = bytes[i = JSONTypeDeserializer.skipWhiteSpacesOrComment(bytes, i, allowComment, parseContext)]) == JSONGeneral.END_ARRAY) && allowLastEndComma)) {
                parseContext.endIndex = i;
                return list;
            }
            if (!isComma) {
                String errorContextTextAt = JSONGeneral.createErrorContextText(bytes, i);
                throw new JSONException("Syntax error, at pos " + i + ", context text by '" + errorContextTextAt + "', unexpected '" + b + "', expected ',' or ']'");
            }
        }
    }

    Map parseJSONObject(CharSource source, byte[] bytes, int fromIndex, Map instance, JSONParseContext parseContext) throws Exception {
        byte b;
        boolean empty = true, allowomment = parseContext.optionConfig.allowComment, disableCacheMapKey = parseContext.optionConfig.disableCacheMapKey;
        int i = fromIndex;
        for (; ; ) {
            if ((b = bytes[++i]) <= ' ') {
                b = bytes[i = JSONGeneral.skipWhiteSpaces(bytes, i + 1)];
            }
            if (allowomment) {
                if (b == '/') {
                    b = bytes[i = JSONGeneral.clearCommentAndWhiteSpaces(bytes, i + 1, parseContext)];
                }
            }
            Serializable key;
            int fieldKeyFrom = i;
            if (b == '"') {
                key = disableCacheMapKey ? JSONTypeDeserializer.parseMapKey(bytes, i, '"', parseContext) : JSONTypeDeserializer.parseMapKeyByCache(bytes, i, '"', parseContext);
                i = parseContext.endIndex;
                empty = false;
                ++i;
            } else {
                if (b == '}') {
                    if (!empty && !parseContext.optionConfig.allowLastEndComma) {
                        throw new JSONException("Syntax error, at pos " + i + ", the closing symbol '}' is not allowed here.");
                    }
                    parseContext.endIndex = i;
                    return instance;
                }
                if (b == '\'') {
                    if (parseContext.optionConfig.allowSingleQuotes) {
                        while (i + 1 < bytes.length && (bytes[++i] != '\'' || bytes[i - 1] == '\\')) ;
                        empty = false;
                        ++i;
                        key = JSONGeneral.parseKeyOfMap(bytes, fieldKeyFrom, i, false);
                    } else {
                        throw new JSONException("Syntax error, at pos " + i + ", the single quote symbol ' is not allowed here.");
                    }
                } else {
                    if (parseContext.optionConfig.allowUnquotedFieldNames) {
                        while (i + 1 < bytes.length && bytes[++i] != ':') ;
                        empty = false;
                        key = JSONGeneral.parseKeyOfMap(bytes, fieldKeyFrom, i, true);
                    } else {
                        int j = i;
                        boolean isNullKey = false;
                        key = null;
                        if (b == 'n' && bytes[++i] == 'u' && bytes[++i] == 'l' && bytes[++i] == 'l') {
                            isNullKey = true;
                            ++i;
                        }
                        if (!isNullKey) {
                            String errorContextTextAt = JSONGeneral.createErrorContextText(bytes, j);
                            throw new JSONException("Syntax error, at pos " + j + ", context text by '" + errorContextTextAt + "', unexpected '" + b + "', expected '\"' or use option ReadOption.AllowUnquotedFieldNames ");
                        }
                    }
                }
            }
            if ((b = bytes[i]) <= ' ') {
                b = bytes[i = JSONGeneral.skipWhiteSpaces(bytes, i + 1)];
            }
            if (allowomment) {
                if (b == '/') {
                    b = bytes[i = JSONGeneral.clearCommentAndWhiteSpaces(bytes, i + 1, parseContext)];
                }
            }
            if (b == ':') {
                if ((b = bytes[++i]) <= ' ') {
                    b = bytes[i = JSONGeneral.skipWhiteSpaces(bytes, i + 1)];
                }
                if (parseContext.optionConfig.allowComment) {
                    if (b == '/') {
                        b = bytes[i = JSONGeneral.clearCommentAndWhiteSpaces(bytes, i + 1, parseContext)];
                    }
                }
                Object value = readValue(source, bytes, i, b, JSONGeneral.END_OBJECT, parseContext);
                i = parseContext.endIndex;
                instance.put(key, value);
                if ((b = bytes[++i]) <= ' ') {
                    b = bytes[i = JSONGeneral.skipWhiteSpaces(bytes, i + 1)];
                }
                if (allowomment) {
                    if (b == '/') {
                        b = bytes[i = JSONGeneral.clearCommentAndWhiteSpaces(bytes, i + 1, parseContext)];
                    }
                }
                if (b == JSONGeneral.COMMA) {
                    continue;
                }
                if (b == JSONGeneral.END_OBJECT) {
                    parseContext.endIndex = i;
                    return instance;
                }
                String errorContextTextAt = JSONGeneral.createErrorContextText(bytes, i);
                throw new JSONException("Syntax error, at pos " + i + ", context text by '" + errorContextTextAt + "', unexpected token '" + (char) b + "', expected ',' or '}'");
            } else {
                throw new JSONException("Syntax error, at pos " + i + ", unexpected '" + (char) b + "', colon ':' is expected.");
            }
        }
    }
}
