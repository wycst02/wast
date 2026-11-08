package io.github.wycst.wast.json;

import io.github.wycst.wast.common.expression.Expression;
import io.github.wycst.wast.common.reflect.GenericParameterizedType;
import io.github.wycst.wast.common.reflect.ReflectConsts;
import io.github.wycst.wast.common.reflect.SetterInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性反序列化模型
 *
 * @Author wangyunchao
 */
public final class JSONPojoFieldDeserializer extends JSONTypeDeserializer implements Comparable<JSONPojoFieldDeserializer> {
    final JSONStore store;
    final String name;
    final int fieldIndex;
    final SetterInfo setterInfo;
    final JSONPropertyDefinition propertyDefinition;
    final long fieldOffset;
    final ReflectConsts.PrimitiveType primitiveType;
    /**
     * 类型信息
     */
    GenericParameterizedType<?> genericParameterizedType;

    /**
     * 类型分类
     */
    final ReflectConsts.ClassCategory classCategory;

    Class<?> implClass;

    /**
     * 反序列化器
     */
    JSONTypeDeserializer deserializer;

    /**
     * 是否自定义反序列器
     */
    boolean customDeserialize = false;

    final String pattern;
    final String timezone;
    boolean flag;

    // 自定义反序列化器
    private final static Map<Class<? extends JSONTypeFieldMapper>, JSONTypeDeserializer> customDeserializers = new ConcurrentHashMap<Class<? extends JSONTypeFieldMapper>, JSONTypeDeserializer>();
    private boolean priority;

    JSONPojoFieldDeserializer(JSONStore store, String name, SetterInfo setterInfo, JSONPropertyDefinition propertyDefinition) {
        this.store = store;
        this.name = name;
        this.setterInfo = setterInfo;
        this.fieldIndex = setterInfo.getIndex();
        this.propertyDefinition = propertyDefinition;
        this.genericParameterizedType = setterInfo.getGenericParameterizedType();
        this.fieldOffset = setterInfo.getFieldOffset();
        this.primitiveType = this.fieldOffset == -1L ? null : setterInfo.getPrimitiveType();
        Class<?> actualClass = genericParameterizedType.getActualType();
        this.classCategory = ReflectConsts.getClassCategory(actualClass);

        String pattern = null;
        String timezone = null;
        if (propertyDefinition != null) {
            pattern = propertyDefinition.pattern();
            timezone = propertyDefinition.timezone();
            if (pattern.isEmpty()) {
                pattern = null;
            }
            if (timezone.isEmpty()) {
                timezone = null;
            }
        }
        this.pattern = pattern;
        this.timezone = timezone;
    }

    void initDeserializer() {
        if (!flag) {
            flag = true;
            Class<?> impl;
            boolean unfixedType = false;
            Class<?>[] possibleTypes = null;
            String possibleExpression = null;
            if (propertyDefinition != null) {
                possibleTypes = propertyDefinition.possibleTypes();
                possibleExpression = propertyDefinition.possibleExpression();
                if ((impl = propertyDefinition.impl()) != Object.class) {
                    if (isAvailableImpl(impl)) {
                        implClass = impl;
                    }
                }
                unfixedType = propertyDefinition.unfixedType();
            }
            if (possibleTypes != null && possibleTypes.length > 0) {
                this.deserializer = store.getPossibleTypesTypeDeserializer(genericParameterizedType.getActualType(), possibleTypes, possibleExpression.isEmpty() ? null : Expression.parse(possibleExpression));
            } else if (implClass != null) {
                this.deserializer = store.getTypeDeserializer(implClass);
                this.genericParameterizedType = GenericParameterizedType.actualType(implClass);
            } else {
                if (setterInfo.isNonInstanceType()) {
                    this.deserializer = store.getCachedTypeDeserializer(genericParameterizedType.getActualType());
                } else {
                    if (genericParameterizedType.getActualClassCategory() == ReflectConsts.ClassCategory.ObjectCategory && unfixedType) {
                        this.deserializer = null;
                    } else {
                        this.deserializer = getDeserializer(genericParameterizedType);
                    }
                }
            }
        }
    }

    boolean ensuredTypeDeserializable() {
        if (setterInfo.isNonInstanceType()) {
            JSONTypeDeserializer deserializer = store.getCachedTypeDeserializer(genericParameterizedType.getActualType());
            if (deserializer == null) {
                return false;
            }
            if (propertyDefinition == null || propertyDefinition.possibleTypes().length == 0) {
                this.deserializer = deserializer;
                flag = true;
            }
        }
        boolean unfixedType = propertyDefinition != null && propertyDefinition.unfixedType();
        if (unfixedType && genericParameterizedType.getActualClassCategory() == ReflectConsts.ClassCategory.ObjectCategory) {
            return false;
        }
        return true;
    }

    private JSONTypeDeserializer getDeserializer(GenericParameterizedType<?> genericParameterizedType) {
        // check custom Deserializer
        if (propertyDefinition != null) {
            try {
                Class<? extends JSONTypeFieldMapper> fieldMapperClass = propertyDefinition.mapper();
                if (fieldMapperClass != JSONTypeFieldMapper.class) {
                    JSONTypeDeserializer customDeserializer = customDeserializers.get(fieldMapperClass);
                    if (customDeserializer != null) {
                        this.customDeserialize = true;
                        return customDeserializer;
                    }
                    JSONTypeFieldMapper<?> fieldMapper = fieldMapperClass.newInstance();
                    if (fieldMapper.deserialize()) {
                        customDeserializer = store.buildDeserializer(fieldMapper);
                        if (fieldMapper.singleton()) {
                            customDeserializers.put(fieldMapperClass, customDeserializer);
                        }
                        this.customDeserialize = true;
                        return customDeserializer;
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return store.getFieldDeserializer(genericParameterizedType, propertyDefinition);
    }

    public String getName() {
        return name;
    }

    public boolean isCustomDeserialize() {
        return customDeserialize;
    }

//    public SetterInfo getSetterInfo() {
//        return setterInfo;
//    }

//    public GenericParameterizedType getGenericParameterizedType() {
//        return genericParameterizedType;
//    }

    protected Object deserialize(CharSource charSource, char[] buf, int fromIndex, GenericParameterizedType<?> parameterizedType, Object defaultValue, int endToken, JSONParseContext jsonParseContext) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected Object deserialize(CharSource charSource, byte[] buf, int fromIndex, GenericParameterizedType<?> parameterizedType, Object defaultValue, int endToken, JSONParseContext jsonParseContext) throws Exception {
        throw new UnsupportedOperationException();
    }

    Object getDefaultFieldValue(Object instance) {
        return JSONGeneral.JSON_SECURE_TRUSTED_ACCESS.getSetterDefault(setterInfo, instance);
    }

    public int getIndex() {
        return setterInfo.getIndex();
    }

//    public void invoke(Object entity, Object value) {
//        JSON_SECURE_TRUSTED_ACCESS.set(setterInfo, entity, value); // setterInfo.invoke(entity, value);
//    }

    public Class<?> getImplClass() {
        return implClass;
    }

    public boolean isAvailableImpl(Class<?> cls) {
        boolean assignableFrom = genericParameterizedType.getActualType().isAssignableFrom(cls);
        if (!assignableFrom) return false;
        // only supported ObjectCategory, CollectionCategory, MapCategory
        switch (classCategory) {
            case MapCategory:
            case CollectionCategory:
            case ObjectCategory:
            case NonInstance:
                return true;
        }
        return false;
    }

    public boolean isInstance(Object value) {
        return value == null || genericParameterizedType.getActualType().isInstance(value);
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(JSONPojoFieldDeserializer o) {
        if (setterInfo != o.setterInfo) return -1;
        return priority ? 1 : 0;
    }

    void deserializeAndSet(CharSource charSource, byte[] buf, int offset, Object entity, JSONParseContext parseContext) throws Exception {
        if (primitiveType != null) {
            switch (primitiveType) {
                case PrimitiveInt: {
                    int value = (Integer) deserializer.deserialize(charSource, buf, offset, genericParameterizedType, null, JSONGeneral.END_OBJECT, parseContext);
                    JSONEndianUnsafe.UNSAFE.putInt(entity, fieldOffset, value);
                    return;
                }
                case PrimitiveLong: {
                    long value = (Long) deserializer.deserialize(charSource, buf, offset, genericParameterizedType, null, JSONGeneral.END_OBJECT, parseContext);
                    JSONEndianUnsafe.UNSAFE.putLong(entity, fieldOffset, value);
                    return;
                }
                case PrimitiveDouble: {
                    double value = JSONGeneral.parseFloats(buf, offset, buf.length, parseContext, true);
                    JSONEndianUnsafe.UNSAFE.putDouble(entity, fieldOffset, value);
                    return;
                }
                case PrimitiveFloat: {
                    float value = (float) JSONGeneral.parseFloats(buf, offset, buf.length, parseContext, false);
                    JSONEndianUnsafe.UNSAFE.putFloat(entity, fieldOffset, value);
                    return;
                }
            }
        }
        // default
        Object value = deserializer.deserialize(charSource, buf, offset, genericParameterizedType, null, JSONGeneral.END_OBJECT, parseContext);
        JSONGeneral.JSON_SECURE_TRUSTED_ACCESS.set(setterInfo, entity, value);
    }

    static JSONPojoFieldDeserializer createFieldDeserializer(JSONStore store, String name, SetterInfo setterInfo, JSONPropertyDefinition propertyDefinition) {
        /*
        final long fieldOffset = setterInfo.getFieldOffset();
        final ReflectConsts.PrimitiveType primitiveType = setterInfo.getPrimitiveType();
        if (primitiveType != null) {
            // jdk9+ has optimization effects
            switch (primitiveType) {
                case PrimitiveInt: {
                    return new JSONPojoFieldDeserializer(store, name, setterInfo, propertyDefinition) {
                        protected void deserialize(CharSource charSource, byte[] buf, int offset, Object entity, JSONParseContext parseContext) throws Exception {
                            int value = (Integer) deserializer.deserialize(charSource, buf, offset, genericParameterizedType, null, JSONGeneral.END_OBJECT, parseContext);
                            JSONEndianUnsafe.UNSAFE.putInt(entity, fieldOffset, value);
                        }
                    };
                }
                case PrimitiveLong: {
                    return new JSONPojoFieldDeserializer(store, name, setterInfo, propertyDefinition) {
                        protected void deserialize(CharSource charSource, byte[] buf, int offset, Object entity, JSONParseContext parseContext) throws Exception {
                            long value = (Long) deserializer.deserialize(charSource, buf, offset, genericParameterizedType, null, JSONGeneral.END_OBJECT, parseContext);
                            JSONEndianUnsafe.UNSAFE.putLong(entity, fieldOffset, value);
                        }
                    };
                }
                case PrimitiveDouble: {
                    return new JSONPojoFieldDeserializer(store, name, setterInfo, propertyDefinition) {
                        protected void deserialize(CharSource charSource, byte[] buf, int offset, Object entity, JSONParseContext parseContext) throws Exception {
                            double value = JSONGeneral.parseFloats(buf, offset, buf.length, parseContext, true);
                            JSONEndianUnsafe.UNSAFE.putDouble(entity, fieldOffset, value);
                        }
                    };
                }
                case PrimitiveFloat: {
                    return new JSONPojoFieldDeserializer(store, name, setterInfo, propertyDefinition) {
                        protected void deserialize(CharSource charSource, byte[] buf, int offset, Object entity, JSONParseContext parseContext) throws Exception {
                            float value = (float) JSONGeneral.parseFloats(buf, offset, buf.length, parseContext, false);
                            JSONEndianUnsafe.UNSAFE.putFloat(entity, fieldOffset, value);
                        }
                    };
                }
            }
        }
        */
        return new JSONPojoFieldDeserializer(store, name, setterInfo, propertyDefinition);
    }
}
