package io.github.wycst.wast.json.options;

/**
 * JSON parsing read options
 *
 * @Author: wangyunchao
 * @Date: 2021/12/26 0:44
 */
public enum ReadOption {

    /**
     * none options
     */
    DefaultNone,

    /***
     * Non‑standard JSON feature: allow comments in JSON, only // and /* *\/ supported, comment parsing is disabled by default
     */
    AllowComment,

    /**
     * Non‑standard JSON feature: allow JSON field keys without double quotes
     */
    AllowUnquotedFieldNames,

    /**
     * Non‑standard JSON feature: allow JSON field keys to use single quotes
     */
    AllowSingleQuotes,

    /**
     * Whether to allow a comma after the last element of object properties or collection elements (non‑JSON standard format)
     */
    AllowLastEndComma,

    /**
     * Target type is byte[], when parsing a string token, convert from hex string to byte array (2 chars to one byte)
     */
    ByteArrayFromHexString,

    /**
     * Unknown enum types throw exception by default; when enabled, parse as null
     */
    UnknownEnumAsNull,

    /**
     * <p>
     * In bean deserialization, if a field type is an ordinary abstract class or interface
     * (excluding Map and Collection and their sub‑interfaces), and a default instance is specified,
     * use that default instance object.
     * This solves type‑mapping issues at usage level, avoiding the security pitfalls of AutoType.
     */
    UseDefaultFieldInstance,

    /**
     * <p>
     * When number type is uncertain, by default map according to the actual number type:
     * integers within Integer range map to Integer, otherwise Long;
     * floating‑point numbers map to Double.
     * When enabled, all uncertain number types are uniformly converted to BigDecimal.
     * <p> BigDecimal has many more fields in memory layout than ordinary number types,
     * even without calling toString it occupies much more memory; not recommended if precision is not required.
     */
    UseBigDecimalAsDefault,

    /**
     * <p> When an empty string does not match any type, return null (empty string means: "")
     */
    UnMatchedEmptyAsNull,

    /**
     * <p> Map parsing caches key values by default.
     * <p> Usually improves parsing performance, but in scenarios with heavy bucket‑index collisions,
     * long linked lists may degrade performance.
     * <p> This option disables cache key.
     */
    DisableCacheMapKey,

    /**
     * Strict mode
     */
    StrictMode;

    // Currently supports up to 32 configuration items; if exceeding 32, change this type to long
    final int value;

    /**
     * Private constructor, automatically generates bit value based on enum ordinal
     */
    ReadOption() {
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

    public static int masks(ReadOption[] options) {
        int mask = 0;
        for (ReadOption option : options) {
            mask |= option.value;  // direct field access, same package
        }
        return mask;
    }
}
