package io.github.wycst.wast.json.options;

public final class WriteOptionConfig {

    // Default configuration (non-final to allow modification)
    static WriteOptionConfig DEFAULT = new WriteOptionConfig(0);

    // Format output with colon space configuration
    public final static WriteOptionConfig FORMAT_OUT_COLON_SPACE = new WriteOptionConfig(WriteOption.FormatOutColonSpace.value);

    // WriteOptionConfig cache
    static WriteOptionConfig[] OPTIONS = new WriteOptionConfig[64];
    static {
        for (int i = 0; i < OPTIONS.length; i++) {
            OPTIONS[i] = new WriteOptionConfig(i);
        }
    }

    /**
     * Option mask
     */
    public final int mask;

    /**
     * Format output
     */
    public final boolean formatOut;

    /**
     * Format output (add a space after colon)
     */
    public final boolean formatOutColonSpace;


    /**
     * Format indentation using space mode (default 4 spaces per level)
     */
    public final boolean formatIndentUseSpace;


    /**
     * Output all attributes
     */
    public final boolean fullProperty;


    /**
     * Skip circular references to prevent dead loops during serialization
     */
    public final boolean skipCircularReference;

    /**
     * Whether to format date
     * Considering the ease of use of dates, 'yyyy-MM-dd HH:mm:ss' will be used regardless of whether date formatting is enabled
     * If you need to serialize date as timestamp, specify DateAsTime
     */
    public final boolean dateFormat;

    /**
     * By default, the date is formatted for output. If this option is configured, it can be serialized as a timestamp
     */
    public final boolean writeDateAsTime;

    /**
     * By default serialize enum class by name, configuring this option will output by Ordinal value, suitable for fixed enum class scenarios.
     * Note: Adding or deleting enum class options will affect deserialization results, use with caution.
     */
    public final boolean writeEnumAsOrdinal;


    /**
     * Output number as string:
     * Note: int, byte, short, double, float, etc. are not affected
     */
    public final boolean writeNumberAsString;

    /**
     * By default, byte array is output as base64 string. When this configuration is enabled, byte array is output as hex string
     */
    public final boolean bytesArrayToHex;

    /**
     * By default, byte array is output as base64 string. When this configuration is enabled, byte array is output as native byte array
     */
    public final boolean bytesArrayToNative;

    /**
     * Whether to ignore escape character validation. Use only when ensuring there are no escape characters needed, can greatly improve performance
     * If there is information that needs escaping such as ", \n, \r, etc., do not enable this configuration
     */
    @Deprecated
    public final boolean ignoreEscapeCheck;

    /**
     * Whether to skip serialization of getter methods that don't have corresponding fields
     */
    public final boolean skipGetterOfNoneField;

    /**
     * Auto close stream (default true)
     */
    public final boolean autoCloseStream;

    /**
     * By default, map keys are output with double quotes
     * When enabled, serialize based on actual key value type
     */
    public final boolean allowUnquotedMapKey;

    /**
     * Use fields of POJO class for serialization
     */
    public final boolean useFields;

    /**
     * Output object properties in underscore mode, eg: userName -> user_name
     */
    public final boolean camelCaseToUnderline;

    /**
     * Whether to write entity class name (POJO) to JSON string when serializing objects (invalid for map and list types)
     */
    public final boolean writeClassName;


    static WriteOptionConfig of(int mask) {
        return mask < OPTIONS.length ? OPTIONS[mask] : new WriteOptionConfig(mask);
    }

    public static WriteOptionConfig of(WriteOption... writeOptions) {
        return writeOptions.length == 0 ? DEFAULT : of(WriteOption.masks(writeOptions));
    }

    // Set default configuration
    public static void setDefault(WriteOption... writeOptions) {
        DEFAULT = of(writeOptions);
    }

    // Set default configuration
    public static void setDefault(WriteOptionConfig optionConfig) {
        DEFAULT = optionConfig.self();
    }

    public static WriteOptionConfig defaultConfig() {
        return DEFAULT;
    }

    public WriteOptionConfig self() {
        return this;
    }

    /**
     * Remove specified options and return a new WriteOptionConfig instance
     *
     * @param writeOptions options to remove
     * @return WriteOptionConfig instance
     */
    public WriteOptionConfig remove(WriteOption... writeOptions) {
        return new WriteOptionConfig(mask & ~WriteOption.masks(writeOptions));
    }

    /**
     * Merge specified options and return a new WriteOptionConfig instance
     *
     * @param options options to merge
     * @return WriteOptionConfig instance
     */
    public WriteOptionConfig merge(WriteOption... options) {
        return new WriteOptionConfig(mask | WriteOption.masks(options));
    }

    public WriteOptionConfig(int mask) {
        this.mask = mask;
        this.formatOut = (mask & WriteOption.FormatOut.value) != 0
                || (mask & WriteOption.FormatOutColonSpace.value) != 0
                || (mask & WriteOption.FormatIndentUseTab.value) != 0
                || (mask & WriteOption.FormatIndentUseSpace.value) != 0
                || (mask & WriteOption.FormatIndentUseSpace8.value) != 0
                || (mask & WriteOption.FormatMaxIndentLevelOne.value) != 0
                || (mask & WriteOption.FormatMaxIndentLevelTwo.value) != 0;
        this.formatOutColonSpace = (mask & WriteOption.FormatOutColonSpace.value) != 0;

        this.formatIndentUseSpace = (mask & WriteOption.FormatIndentUseTab.value) == 0 
                && ((mask & WriteOption.FormatIndentUseSpace.value) != 0
                || (mask & WriteOption.FormatIndentUseSpace8.value) != 0);

        this.fullProperty = (mask & WriteOption.FullProperty.value) != 0 && (mask & WriteOption.IgnoreNullProperty.value) == 0;

        this.skipCircularReference = (mask & WriteOption.SkipCircularReference.value) != 0;
        this.dateFormat = (mask & WriteOption.DateFormat.value) != 0;
        this.writeDateAsTime = (mask & WriteOption.DateAsTime.value) != 0;
        this.writeEnumAsOrdinal = (mask & WriteOption.WriteEnumAsOrdinal.value) != 0 && (mask & WriteOption.WriteEnumAsName.value) == 0;

        this.writeNumberAsString = (mask & WriteOption.NumberAsString.value) != 0;
        this.bytesArrayToHex = (mask & WriteOption.BytesArrayToHex.value) != 0;
        this.bytesArrayToNative = (mask & WriteOption.BytesArrayToNative.value) != 0;
        this.ignoreEscapeCheck = (mask & WriteOption.IgnoreEscapeCheck.value) != 0;
        this.skipGetterOfNoneField = (mask & WriteOption.SkipGetterOfNoneField.value) != 0;
        this.autoCloseStream = (mask & WriteOption.KeepOpenStream.value) == 0;
        this.allowUnquotedMapKey = (mask & WriteOption.AllowUnquotedMapKey.value) != 0;
        this.useFields = (mask & WriteOption.UseFields.value) != 0;
        this.camelCaseToUnderline = (mask & WriteOption.CamelCaseToUnderline.value) != 0;
        this.writeClassName = (mask & WriteOption.WriteClassName.value) != 0;
    }
}
