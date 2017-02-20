package zhp.plugin.second;

import android.os.Bundle;
import android.widget.TextView;

import zhp.plugin.sdk.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("这是插件一的Activity");
        setContentView(tv);
    }
}
