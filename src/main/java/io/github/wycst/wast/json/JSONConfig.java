package io.github.wycst.wast.json;

import io.github.wycst.wast.json.options.WriteOption;
import io.github.wycst.wast.json.options.WriteOptionConfig;

import java.util.TimeZone;

public final class JSONConfig {

    // shared singleton config for the pooled writer; never customized
    static final JSONConfig DEFAULT = new JSONConfig();

    // options config (immutable); mutable serialization state moved to JSONWriter
    WriteOptionConfig optionConfig;

    /**
     * 缩进空格数量,默认4个空格
     */
    int formatIndentSpaceNum = 4;

    /**
     * 最大缩进级别（默认不限制,最小为1）
     */
    int maxIndentLevel = Integer.MAX_VALUE;

    /**
     * 日期格式默认： YYYY-MM-dd HH:mm:ss
     */
    private String dateFormatPattern = "YYYY-MM-dd HH:mm:ss";

    /**
     * 指定时区
     */
    TimeZone timezone;

    public JSONConfig() {
        this(WriteOptionConfig.defaultConfig());
    }

    public JSONConfig(WriteOptionConfig optionConfig) {
        this.optionConfig = optionConfig.self();
    }

    public JSONConfig(WriteOption... options) {
        this(WriteOptionConfig.of(options));
    }

    public static JSONConfig of(WriteOption... options) {
        if (options.length == 0) {
            return DEFAULT;
        }
        return new JSONConfig(WriteOptionConfig.of(options));
    }

    public static JSONConfig formatOf() {
        return new JSONConfig(WriteOption.FormatOutColonSpace);
    }

    public void mergeOptions(WriteOption... options) {
        this.optionConfig = optionConfig.merge(options);
    }

    public boolean isFormatIndentUseSpace() {
        return optionConfig.formatIndentUseSpace;
    }

    public int getFormatIndentSpaceNum() {
        return formatIndentSpaceNum;
    }

    public void setFormatIndentSpaceNum(int formatIndentSpaceNum) {
        this.formatIndentSpaceNum = Math.max(formatIndentSpaceNum, 1);
    }

    public JSONConfig formatIndentSpaceNum(int formatIndentSpaceNum) {
        setFormatIndentSpaceNum(formatIndentSpaceNum);
        return this;
    }

    public void setMaxIndentLevel(int maxIndentLevel) {
        this.maxIndentLevel = Math.max(maxIndentLevel, 1);
    }

    public JSONConfig maxIndentLevel(int maxFormatOutIndentLevel) {
        setMaxIndentLevel(maxFormatOutIndentLevel);
        return this;
    }

    public boolean isWriteDateAsTime() {
        return optionConfig.writeDateAsTime;
    }

    public String getDateFormatPattern() {
        return dateFormatPattern;
    }

    public void setDateFormatPattern(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    public boolean isSkipCircularReference() {
        return optionConfig.skipCircularReference;
    }

    public boolean isBytesArrayToNative() {
        return optionConfig.bytesArrayToNative;
    }

    public boolean isFormatOut() {
        return optionConfig.formatOut;
    }

    public void setFormatOut(boolean b) {
        if(b) {
            optionConfig = optionConfig.merge(WriteOption.FormatOutColonSpace);
        } else {
            optionConfig = optionConfig.remove(WriteOption.FormatOutColonSpace);
        }
    }

    public boolean isFormatOutColonSpace() {
        return optionConfig.formatOutColonSpace;
    }

    public boolean isFullProperty() {
        return optionConfig.fullProperty;
    }

    public boolean isDateFormat() {
        return optionConfig.dateFormat;
    }

    public boolean isBytesArrayToHex() {
        return optionConfig.bytesArrayToHex;
    }

    @Deprecated
    public boolean isIgnoreEscapeCheck() {
        return optionConfig.ignoreEscapeCheck;
    }

    public boolean isSkipGetterOfNoneField() {
        return optionConfig.skipGetterOfNoneField;
    }

    public boolean isAutoCloseStream() {
        return optionConfig.autoCloseStream;
    }

    public boolean isAllowUnquotedMapKey() {
        return optionConfig.allowUnquotedMapKey;
    }

    public boolean isUseFields() {
        return optionConfig.useFields;
    }

    public boolean isCamelCaseToUnderline() {
        return optionConfig.camelCaseToUnderline;
    }

    public boolean isWriteEnumAsOrdinal() {
        return optionConfig.writeEnumAsOrdinal;
    }

    public void setWriteEnumAsOrdinal(boolean writeEnumAsOrdinal) {
        if(writeEnumAsOrdinal) {
            optionConfig = optionConfig.merge(WriteOption.WriteEnumAsOrdinal).remove(WriteOption.WriteEnumAsName);
        } else {
            optionConfig = optionConfig.remove(WriteOption.WriteEnumAsOrdinal);
        }
    }

    public boolean isWriteNumberAsString() {
        return optionConfig.writeNumberAsString;
    }

    public boolean isWriteClassName() {
        return optionConfig.writeClassName;
    }

    public void clear() {
    }
}
