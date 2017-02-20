package zhp.plugin;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author 郑海鹏
 * @since 2015/11/20 17:03
 */
public class ClassInject {
	Object pathDexElements = null;

    /**
     *  将DexClassLoader的PathList注入到PathCLassLoader中去。
     */
    public void inject(PathClassLoader pathLoader, DexClassLoader dexLoader) {
        try {
            // 1. 获得PathList
            Object pathLoaderPathList = getPathList(pathLoader);
            Object dexLoaderPathList = getPathList(dexLoader);

            // 2. 获得DexElements
            if(pathDexElements == null){
            	pathDexElements = getDexElements(pathLoaderPathList);
            }
            Object dexDexElements = getDexElements(dexLoaderPathList);

            // 3. 合并为新的DexElements
            Object dexElements = combineArray(dexDexElements, pathDexElements);

            // 4. 注入回pathLoader的PathList中去
            setField(pathLoaderPathList, pathLoaderPathList.getClass(), "dexElements", dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得PathList
     */
    private Object getPathList(Object classLoader) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(classLoader, Class.forName("dalvik.system.BaseDexClassLoader"),
                "pathList");
    }

    /**
     * 获得DexElements
     */
    private Object getDexElements(Object paramObject) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }

    /**
     * 获得obj对象的名为fieldName的变量的值。
     * @param obj 要获取变量值的对象
     * @param classObject   参数obj的类型。
     *                      因为我们要找的那个变量可能是在obj.getClass()的父类中声明的。
     *                      如果直接使用obj.getClass().getDeclaredField(fieldName)会
     *                      抛出NoSuchFieldException异常。
     * @param fieldName 变量的名字
     */
    private Object getField(Object obj, Class<?> classObject, String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field localField = classObject.getDeclaredField(fieldName);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 将obj对象的field变量的值设置为value
     */
    private void setField(Object obj, Class<?> classObject, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = classObject.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    /**
     * 合并两个数组
     */
    private Object combineArray(Object array1, Object array2) {
        Class<?> localClass = array1.getClass().getComponentType();
        int length1 = Array.getLength(array1);
        int lengthSum = length1 + Array.getLength(array2);

        // 新建一个数组，类型为localClass, 长度为两者之和。
        Object result = Array.newInstance(localClass, lengthSum);
        for (int k = 0; k < lengthSum; ++k) {
            if (k < length1) {
                Array.set(result, k, Array.get(array1, k));
            } else {
                Array.set(result, k, Array.get(array2, k - length1));
            }
        }
        return result;
    }

}
