package io.github.wycst.wast.json.options;

/**
 * JSON serialization configuration options
 *
 * @author wangy
 */

public enum WriteOption {

    /**
     * none options
     */
    DefaultNone,

    /**
     * Format indented output (default uses tab character \t)
     */
    FormatOut,

    /**
     * Whether to add a space after the colon when outputting object properties with formatted indentation
     */
    FormatOutColonSpace,

    /**
     * Format indentation using TAB mode (default configuration)
     */
    FormatIndentUseTab,

    /**
     * Format indentation using space mode (default 4 spaces per level)
     */
    FormatIndentUseSpace,

    /**
     * Format indentation using 8 spaces per level indentation mode
     */
    FormatIndentUseSpace8,

    /**
     * Quick setting for maximum first-level indentation
     *
     * @see io.github.wycst.wast.json.JSONConfig#setMaxIndentLevel(int)
     */
    FormatMaxIndentLevelOne,

    /**
     * Quick setting for maximum second-level indentation
     *
     * @see io.github.wycst.wast.json.JSONConfig#setMaxIndentLevel(int)
     */
    FormatMaxIndentLevelTwo,

    /**
     * Output all attributes
     */
    FullProperty,

    /**
     * Filter null fields (default configuration)
     */
    IgnoreNullProperty,

    /**
     * Skip circular references to prevent dead loops during serialization
     */
    SkipCircularReference,

    /**
     * <p> Whether to format date
     * Considering the ease of use of dates, 'yyyy-MM-dd HH:mm:ss' will be used regardless of whether date formatting is enabled
     * If you need to serialize date as timestamp, specify DateAsTime
     */
    DateFormat,

    /**
     * <p>By default, the date is formatted for output. If this option is configured, it can be serialized as a timestamp
     */
    DateAsTime,

    /**
     * <p> By default serialize enum class by name, configuring this option will output by Ordinal value, suitable for fixed enum class scenarios.
     * <p> Note: Adding or deleting enum class options will affect deserialization results, use with caution.
     */
    WriteEnumAsOrdinal,

    /**
     * <p> Write enum by its name when serializing enumerations. (default configuration)
     */
    WriteEnumAsName,

    /**
     * <p> Output number as string:
     * <p> Note: int, byte, short, double, float, etc. are not affected
     */
    NumberAsString,

    /**
     * By default, byte array is output as base64 string. When this configuration is enabled, byte array is output as hex string
     */
    BytesArrayToHex,

    /**
     * By default, byte array is output as base64 string. When this configuration is enabled, byte array is output as native byte array
     */
    BytesArrayToNative,

    /***
     *
     * <p> Whether to ignore escape character validation. Use only when ensuring there are no escape characters needed, can greatly improve performance
     * <p> If there is information that needs escaping such as ", \n, \r, etc., do not enable this configuration
     *
     */
    @Deprecated
    IgnoreEscapeCheck,

    /***
     * Whether to skip serialization of getter methods that don't have corresponding fields
     */
    SkipGetterOfNoneField,

    /**
     * <p> By default, map keys are output with double quotes
     * <p> When enabled, serialize based on actual key value type
     */
    AllowUnquotedMapKey,

    /**
     * Use fields of POJO class for serialization
     */
    UseFields,

    /**
     * Output object properties in underscore mode, eg: userName -> user_name
     */
    CamelCaseToUnderline,

    /**
     * Do not close stream after serialization. By default, stream is automatically closed. When enabled, close() won't be called
     */
    KeepOpenStream,

    /**
     * Whether to write entity class name (POJO) to JSON string when serializing objects (invalid for map and list types)
     */
    WriteClassName;

    // Currently supports up to 32 configuration items; if exceeding 32, change this type to long
    final int value;

    /**
     * Private constructor, automatically generates bit value based on enum ordinal
     */
    WriteOption() {
        this.value = 1 << ordinal();
    }

    /**
     * Get the bit value corresponding to the enum
     *
     * @return bit value (1 << ordinal())
     */
    public int getValue() {
        return value;
    }

    public static int masks(WriteOption[] options) {
        int mask = 0;
        for (WriteOption option : options) {
            mask |= option.value;  // direct field access, same package
        }
        return mask;
    }
}
