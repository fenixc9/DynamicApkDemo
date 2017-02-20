package zhp.plugin;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Click(View v) {
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra(ProxyActivity.PROXIED_CLASS_NAME, "zhp.plugin.first.MainActivity");
        intent.putExtra(ProxyActivity.EXTRA_APK_PATH, Environment.getExternalStorageDirectory() + File.separator + "plugin1.apk");
        startActivity(intent);
    }

    public void onButton2Click(View view){
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra(ProxyActivity.PROXIED_CLASS_NAME, "zhp.plugin.second.MainActivity");
        intent.putExtra(ProxyActivity.EXTRA_APK_PATH, Environment.getExternalStorageDirectory() + File.separator + "plugin2.apk");
        startActivity(intent);
    }
}
