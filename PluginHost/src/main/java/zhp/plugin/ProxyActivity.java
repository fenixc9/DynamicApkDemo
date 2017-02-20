package zhp.plugin;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 宿主的代理类
 */
public class ProxyActivity extends Activity {
	// ==========================================
	// Fields
	// ==========================================
	public final static String PROXIED_CLASS_NAME = "PROXIED_CLASS_NAME";
	public final static String EXTRA_APK_PATH = "EXTRA_APK_PATH";
	Object proxiedActivity = null;
	DexClassLoader classLoader;
	
	
	// ==========================================
	// Override / Implement Methods
	// ==========================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化ClassLoader
		initClassLoader();
		
		// 开始加载被代理类
		loadProxiedActivity();
		
		// 走伪周期
		if(proxiedActivity != null){
			android.util.Log.i("郑海鹏", "ProxyActivity#onCreate(): " + "伪周期开始！");
			try {
				Method method = proxiedActivity.getClass().getMethod("onCreate", Bundle.class);
				method.setAccessible(true);
				method.invoke(proxiedActivity, savedInstanceState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			android.util.Log.i("郑海鹏", "ProxyActivity#onCreate(): " + "被代理类为null");
		}
	}

	@Override
	protected void onDestroy() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onDestroy", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onPause", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onPause();
	}

	@Override
	protected void onRestart() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onRestart", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onRestart();
	}

	@Override
	protected void onResume() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onResume", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onResume();
	}

	@Override
	protected void onStart() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onStart", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		if(proxiedActivity != null){
			try {
				Method method = proxiedActivity.getClass().getMethod("onStop", new Class[]{});
				method.setAccessible(true);
				method.invoke(proxiedActivity, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onStop();
	}

	// ==========================================
	// Methods
	// ==========================================
	/**
	 * 加载被代理Activity的信息
	 */
	public void loadProxiedActivity(){		
		// 实例化被代理类
		String className = getIntent().getStringExtra(PROXIED_CLASS_NAME);
		android.util.Log.i("郑海鹏", "ProxyActivity#initProxiedActivity(): " + "传入的类名为：\n" + className);
		try {
			Class<?> clazz = classLoader.loadClass(className);
			proxiedActivity = clazz.newInstance();
			// 使得插件的Activity持有宿主Activity的引用
			Method method = proxiedActivity.getClass().getMethod("setProxy", Activity.class);
			method.setAccessible(true);
			method.invoke(proxiedActivity, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 初始化classLoader
     */
    private void initClassLoader() {
        // 插件放在sd卡的根目录下
    	String apkPath = getIntent().getStringExtra(EXTRA_APK_PATH);


		File file = new File(apkPath);
		if(file.exists()){
			Log.i("郑海鹏", "文件存在！");
		}else{
			Log.i("郑海鹏", "文件不存在！");
		}

        // dex文件的释放目录
        File releasePath = getDir("dexs", 0);

        // 类加载器
        classLoader = new DexClassLoader(apkPath, releasePath.getAbsolutePath(), null, getClassLoader());
        
        
        // 注入到原生的ClassLoader中
        ClassInject inject = new ClassInject();
        inject.inject((PathClassLoader) getClassLoader(), classLoader);
    }
}
