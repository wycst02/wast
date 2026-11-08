package io.github.wycst.wast.json.options;

public final class ReadOptionConfig {

    // Default configuration
    public final static ReadOptionConfig DEFAULT = new ReadOptionConfig(0);

    // ReadOptionConfig cache
    static ReadOptionConfig[] OPTIONS = new ReadOptionConfig[64];
    static {
        for (int i = 0; i < OPTIONS.length; i++) {
            OPTIONS[i] = new ReadOptionConfig(i);
        }
    }

    // Lax syntax configuration (allowLastEndComma, allowSingleQuotes, allowUnquotedFieldNames)
    public final static ReadOptionConfig LAX_ALE_ASQ_AUFN = new ReadOptionConfig(
            ReadOption.AllowLastEndComma.value |
                    ReadOption.AllowSingleQuotes.value |
                    ReadOption.AllowUnquotedFieldNames.value
    );

    /**
     * Option mask
     */
    public final int mask;

    /**
     * Allow comments
     */
    public final boolean allowComment;

    /**
     * Allow key fields to use single quotes
     */
    public final boolean allowSingleQuotes;

    /**
     * Allow key fields without double quotes
     */
    public final boolean allowUnquotedFieldNames;

    /**
     * Whether to allow a comma after the last element of object properties or collection elements (non‑JSON standard format)
     */
    public final boolean allowLastEndComma;

    /**
     * Use BigDecimal as default number parsing
     */
    public final boolean useBigDecimalAsDefault;

    /**
     * Whether to parse unknown enum types as null
     */
    public final boolean unknownEnumAsNull;

    /**
     * Enable default field instance when interface or abstract class cannot be instantiated
     */
    public final boolean useDefaultFieldInstance;

    /**
     * Disable cache key
     */
    public final boolean disableCacheMapKey;
    /**
     * Convert from hex string to byte array
     */
    public final boolean byteArrayFromHexString;

    public final boolean unMatchedEmptyAsNull;
    public final boolean strictMode;

    public static ReadOptionConfig of(ReadOption... readOptions) {
        return readOptions.length == 0 ? DEFAULT : of(ReadOption.masks(readOptions));
    }

    static ReadOptionConfig of(int mask) {
        return mask < OPTIONS.length ? OPTIONS[mask] : new ReadOptionConfig(mask);
    }

    /**
     * Remove specified options and return a new ReadOptionConfig instance
     *
     * @param readOptions options to remove
     * @return ReadOptionConfig instance
     */
    public ReadOptionConfig remove(ReadOption... readOptions) {
        return new ReadOptionConfig(mask & ~ReadOption.masks(readOptions));
    }

    /**
     * Merge specified options and return a new ReadOptionConfig instance
     *
     * @param options options to merge
     * @return ReadOptionConfig instance
     */
    public ReadOptionConfig merge(ReadOption... options) {
        return new ReadOptionConfig(mask | ReadOption.masks(options));
    }

    public ReadOptionConfig(int mask) {
        this.mask = mask;
        this.byteArrayFromHexString = (mask & ReadOption.ByteArrayFromHexString.value) != 0;
        this.unknownEnumAsNull = (mask & ReadOption.UnknownEnumAsNull.value) != 0;
        this.allowSingleQuotes = (mask & ReadOption.AllowSingleQuotes.value) != 0;
        this.allowUnquotedFieldNames = (mask & ReadOption.AllowUnquotedFieldNames.value) != 0;
        this.allowComment = (mask & ReadOption.AllowComment.value) != 0;
        this.allowLastEndComma = (mask & ReadOption.AllowLastEndComma.value) != 0;
        this.useDefaultFieldInstance = (mask & ReadOption.UseDefaultFieldInstance.value) != 0;
        this.useBigDecimalAsDefault = (mask & ReadOption.UseBigDecimalAsDefault.value) != 0;
        this.disableCacheMapKey = (mask & ReadOption.DisableCacheMapKey.value) != 0;
        this.unMatchedEmptyAsNull = (mask & ReadOption.UnMatchedEmptyAsNull.value) != 0;
        this.strictMode = (mask & ReadOption.StrictMode.value) != 0;
        // this.ignoreEscapeCheck = (mask & ReadOption.IgnoreEscapeCheck.value) != 0;
    }
}
