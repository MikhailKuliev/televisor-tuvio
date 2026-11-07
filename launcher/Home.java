package local.tuvio.home;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

public class Home extends Activity {
    final String[] names={"YouTube","TV Bro","Happ","Кинопоиск","VK Видео","Файлы","Настройки"};
    final String[] packages={"com.yandex.tv.ytplayer","com.phlox.tvwebbrowser","su.happ.proxyutility","ru.kinopoisk.yandex.tv","com.vk.tv","com.cultraview.newfilemanager",""};
    final int[] colors={0xffb52626,0xff165cab,0xff337164,0xffb64913,0xff165cab,0xff624478,0xff53617a};
    int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    boolean library; Button libraryTile; public void onCreate(Bundle state){super.onCreate(state); render(false);} @Override public void onBackPressed(){if(library){render(false);libraryTile.requestFocus();}} void render(boolean all){
        library=all;
        LinearLayout page=new LinearLayout(this);
        page.setOrientation(1);
        page.setPadding(dp(48),dp(30),dp(48),dp(30));
        page.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{0xff354773,0xff181421}));
        setContentView(page);
        LinearLayout header=new LinearLayout(this);
        page.addView(header);
        TextView title=new TextView(this);
        title.setText(all?"Приложения":"Главный экран"); title.setTextSize(32); title.setTextColor(Color.WHITE);
        header.addView(title,new LinearLayout.LayoutParams(0,-2,1));
        TextClock clock=new TextClock(this); clock.setTextSize(20); header.addView(clock);
        ScrollView scroll=new ScrollView(this);
        page.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout grid=new LinearLayout(this); grid.setOrientation(1); scroll.addView(grid);
        java.util.LinkedHashMap<String,String> apps=new java.util.LinkedHashMap<>();
        if(!all){
            for(int i=0;i<3;i++) apps.put(packages[i],names[i]);
            apps.put("#library","Приложения"); apps.put("com.yandex.tv.music","Яндекс Музыка");
            apps.put("org.fdroid.fdroid","F-Droid");
            for(int i=3;i<packages.length;i++)apps.put(packages[i],names[i]);
        }else{
            java.util.TreeMap<String,String> sorted=new java.util.TreeMap<>();
            for(String category:new String[]{Intent.CATEGORY_LAUNCHER,Intent.CATEGORY_LEANBACK_LAUNCHER}){
                Intent query=new Intent(Intent.ACTION_MAIN).addCategory(category);
                for(android.content.pm.ResolveInfo app:getPackageManager().queryIntentActivities(query,0)){
                    String pkg=app.activityInfo.packageName;
                    if(!pkg.equals("com.yandex.tv.home")&&!pkg.equals(getPackageName())&&!pkg.equals("mediatek.factorymenu.ui")){
                        String label=app.loadLabel(getPackageManager()).toString();
                        sorted.put(label+"\n"+pkg,pkg);
                    }
                }
            }
            for(String label:sorted.keySet())apps.put(sorted.get(label),label.split("\n")[0]);
            apps.put("com.yandex.tv.music","Яндекс Музыка");
        }
        int index=0; LinearLayout row=null;
        for(java.util.Map.Entry<String,String> app:apps.entrySet()){
            if(index%3==0){row=new LinearLayout(this);row.setWeightSum(3);grid.addView(row);}
            String pkg=app.getKey(); Button tile=new Button(this);
            tile.setAllCaps(false); tile.setText(app.getValue()); tile.setTextColor(Color.WHITE);tile.setTextSize(20);
            GradientDrawable bg=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{colors[index%colors.length],0xff202339});
            bg.setCornerRadius(dp(15));tile.setBackground(bg);
            tile.setOnFocusChangeListener((v,f)->{bg.setStroke(dp(3),f?Color.WHITE:Color.TRANSPARENT);v.animate().scaleX(f?1.035f:1f).scaleY(f?1.035f:1f).setDuration(110).start();});
            tile.setOnClickListener(v->{if(pkg.equals("#library"))render(true);else open(pkg,app.getValue());});
            LinearLayout.LayoutParams cell=new LinearLayout.LayoutParams(0,dp(93),1);cell.setMargins(dp(8),dp(8),dp(8),dp(8));row.addView(tile,cell);
            if(pkg.equals("#library"))libraryTile=tile;
            if(index++==0)tile.requestFocus();
        }
    }
    void open(String pkg,String label){
        Intent intent=pkg.isEmpty()?new Intent(Settings.ACTION_SETTINGS):getPackageManager().getLeanbackLaunchIntentForPackage(pkg);
        if(pkg.equals("com.yandex.tv.music"))intent=new Intent("com.yandex.tv.music.SHOW_MUSIC_PLAYER").setPackage(pkg);
        if(intent==null)intent=getPackageManager().getLaunchIntentForPackage(pkg);
        if(intent!=null){try{startActivity(intent);return;}catch(RuntimeException ignored){}}
        Toast.makeText(this,"Не удалось открыть "+label,Toast.LENGTH_SHORT).show();
    }
}
