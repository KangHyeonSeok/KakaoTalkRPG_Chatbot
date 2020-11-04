package com.whyk.notilistener;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by KangHyeonSeok on 16. 5. 30..
 */
public class JsonSerializer {
    private static final Class<?>[] PRIMITIVE_TYPES = { int.class, long.class, short.class,
            float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class,
            Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };


    static public String toJson(Object object) {
        JSONObject jsonObject = new JSONObject();

        toJsonObject(object, jsonObject);

        return jsonObject.toString();
    }


    static public <T> T fromJson(String jsonData, Class<T> classOfT) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Object object = classOfT.newInstance();

            fromJsonObject(object, jsonObject);

            return (T)object;
        }catch (Exception e) {
            Log.d("JSON",e.toString());
        }
        return null;
    }

    static private void fromJsonObject(Object object, JSONObject jsonObject) {
        Class objectClass = object.getClass();
        Field [] fieldArray = objectClass.getFields();

        for (Field field:fieldArray ) {
            try {
                if( Modifier.isStatic(field.getModifiers()) )
                    continue;

                Object fieldValue;

                if( field.getType().isPrimitive() )
                    fieldValue = field.get(object);
                else {
                    if( field.getGenericType() instanceof ParameterizedType )
                        fieldValue = new ArrayList<>();
                    else if( field.getType().isArray() ) {
                        fieldValue = Array.newInstance(field.getType().getComponentType(), jsonObject.getJSONArray(field.getName()).length());
                    }
                    else {
                        fieldValue = field.getType().newInstance();
                    }
                }

                if (isPrimitiveOrString(fieldValue)) {
                    if( field.getGenericType() == float.class )
                        field.set(object, ((Double)(jsonObject.get(field.getName()))).floatValue() );
                    else {
                        Object j = jsonObject.get((field.getName()));
                        field.set(object, jsonObject.get(field.getName()));
                    }
                }
                else if( fieldValue.getClass().isArray() ) {
                    JSONArray jsonArray = jsonObject.getJSONArray(field.getName());
                    for( int i = 0; i < jsonArray.length();i++ ) {
                        if( isPrimitiveOrString(jsonArray.get(i)) )
                            Array.set(fieldValue,i,jsonArray.get(i));
                        else {
                            Object arrayItem = fieldValue.getClass().getComponentType().newInstance();
                            fromJsonObject(arrayItem, jsonArray.getJSONObject(i));
                            Array.set(fieldValue,i,arrayItem);
                        }
                    }
                }else if( fieldValue instanceof List<?> ) {
                    JSONArray jsonArray = jsonObject.getJSONArray(field.getName());
                    for( int i = 0; i < jsonArray.length();i++ ) {
                        if( isPrimitiveOrString(jsonArray.get(i)) )
                            ((List<Object>)fieldValue).add(i,jsonArray.get(i));
                        else {
                            ParameterizedType listType = (ParameterizedType) field.getGenericType();
                            Class<?> listArgumentClass = (Class<?>) listType.getActualTypeArguments()[0];
                            Object listItem = listArgumentClass.newInstance();

                            fromJsonObject(listItem, jsonArray.getJSONObject(i));
                            ((List<Object>)fieldValue).add(i,listItem);
                        }
                    }
                }else {
                    JSONObject subJsonObject = jsonObject.getJSONObject(field.getName());
                    fromJsonObject(fieldValue, subJsonObject);
                }

                if( !isPrimitiveOrString(fieldValue))
                    field.set(object, fieldValue);
            }catch (Exception e) {
                Log.d("JSON", objectClass.getName());
                Log.d("JSON", field.getName());
                Log.e("JSON", e.toString());

            }
        }
    }

    static private void toJsonObject(Object object, JSONObject jsonObject) {

        Class objectClass = object.getClass();
        Field [] fieldArray = objectClass.getFields();

        /*
        Arrays.sort(fieldArray, new Comparator<Field>() {
            @Override
            public int compare(Field lhs, Field rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        */

        for (Field field:fieldArray ) {
            try {
                if( Modifier.isStatic(field.getModifiers()) )
                    continue;

                Object fieldValue = field.get(object);
                if (isPrimitiveOrString(fieldValue))
                    jsonObject.put(field.getName(), fieldValue);
                else if( fieldValue.getClass().isArray() ) {
                    JSONArray jsonArray = new JSONArray();
                    for( int i = 0; i < Array.getLength(fieldValue);i++ ) {
                        Object arrayItem = Array.get(fieldValue, i);
                        putArrayItem(arrayItem, jsonArray);
                    }
                    jsonObject.put(field.getName(), jsonArray);
                }else if( fieldValue instanceof List<?> ) {
                    JSONArray jsonArray = new JSONArray();
                    for (Object listItem:(List)fieldValue)
                        putArrayItem(listItem, jsonArray);
                    jsonObject.put(field.getName(), jsonArray);
                }
                else {
                    JSONObject subJsonObject = new JSONObject();
                    toJsonObject(fieldValue, subJsonObject);
                    jsonObject.put(field.getName(),subJsonObject);
                }
            }catch (Exception e) {
                Log.d("JSON", objectClass.getName());
                Log.d("JSON", field.getName());
                Log.e("JSON", e.toString());

            }

        }
    }

    private static void putArrayItem(Object arrayItem, JSONArray jsonArray) {
        if( isPrimitiveOrString(arrayItem) )
            jsonArray.put(arrayItem);
        else if( !arrayItem.getClass().isArray() ) {
            JSONObject arraySubJsonObject = new JSONObject();
            toJsonObject(arrayItem, arraySubJsonObject);
            jsonArray.put(arraySubJsonObject);
        }
    }


    private static boolean isPrimitiveOrString(Object target) {
        if (target instanceof String) {
            return true;
        }

        Class<?> classOfPrimitive = target.getClass();
        for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
                return true;
            }
        }
        return false;
    }
}
