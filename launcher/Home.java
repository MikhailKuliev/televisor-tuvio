package local.tuvio.home;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class Home extends Activity {
    static final String[] FEATURE={"com.yandex.tv.ytplayer","com.fgl27.twitch","com.phlox.tvwebbrowser","com.vk.tv"};
    static final String[] TOOLS={"su.happ.proxyutility","com.yandex.tv.music","org.fdroid.fdroid","com.cultraview.newfilemanager"};
    LinearLayout page,grid,row;
    boolean library;
    Button more;
    int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    public void onCreate(Bundle state){super.onCreate(state); render(false);}
    public void onBackPressed(){if(library){render(false);more.requestFocus();}}
    GradientDrawable paint(int a,int b,int radius){
        GradientDrawable g=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{a,b});
        g.setCornerRadius(dp(radius));return g;
    }
    TextView text(String label,int size){
        TextView t=new TextView(this);t.setText(label);t.setTextSize(size);t.setTextColor(-1);
        t.setTypeface(Typeface.create("sans-serif-medium",0));return t;
    }
    void focus(View v,GradientDrawable bg){
        v.setFocusable(true);v.setBackground(bg);
        v.setOnFocusChangeListener((w,f)->{bg.setStroke(dp(f?3:1),f?-1:0x335da6ff);
            w.animate().scaleX(f?1.025f:1f).scaleY(f?1.025f:1f).setDuration(100).start();});
    }
    Button tab(LinearLayout bar,String label,boolean active,Runnable action){
        Button b=new Button(this);b.setAllCaps(false);b.setText(label);b.setTextSize(16);b.setTextColor(-1);
        focus(b,paint(active?0xff3466bd:0xff193967,0xff183b71,24));b.setOnClickListener(v->action.run());
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-2,dp(43));lp.rightMargin=dp(8);bar.addView(b,lp);return b;
    }
    void render(boolean all){
        stopAlbum(); library=all;page=new LinearLayout(this);page.setOrientation(1);page.setPadding(dp(35),dp(22),dp(35),dp(12));
        page.setBackground(paint(0xff264d9c,0xff0a2047,0));setContentView(page);
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER_VERTICAL);page.addView(nav);
        tab(nav,"Главная",!all,()->render(false));more=tab(nav,"Остальные",all,()->render(true));
        tab(nav,"Настройки",false,()->open("","Настройки"));
        nav.addView(new Space(this),new LinearLayout.LayoutParams(0,1,1));
        TextClock clock=new TextClock(this);clock.setFormat24Hour("HH:mm");clock.setTextColor(-1);clock.setTextSize(30);nav.addView(clock);
        ScrollView sv=new ScrollView(this);page.addView(sv);grid=new LinearLayout(this);grid.setOrientation(1);sv.addView(grid);
        if(all){showLibrary();more.requestFocus();return;}
        section("Смотреть",30);startRow(4);
        card(FEATURE[0],"YouTube","Видео",0xff245eab,0xff102f65,true).requestFocus();
        card(FEATURE[1],"Twitch","Трансляции",0xff6652b4,0xff342c79,true);
        card(FEATURE[2],"TV Bro","Браузер",0xff22778f,0xff16416f,true);
        card(FEATURE[3],"VK Видео","Видео",0xff397be1,0xff224582,true);
        addResumePanel();
        section("Под рукой",20);startRow(4);
        String[] names={"Happ","Яндекс Музыка","F-Droid","Файлы"};
        for(int i=0;i<TOOLS.length;i++)card(TOOLS[i],names[i],"",0xff2c528b,0xff183666,false);
    }
    final android.os.Handler timer=new android.os.Handler(android.os.Looper.getMainLooper());
    ImageView photo;
    int slide;
    boolean visible;
    TextView resumeLabel;
    final Runnable rotate=new Runnable(){public void run(){
        if(!visible || library || photo==null)return;
        java.io.File base=getExternalFilesDir(null);
        if(base==null)return;
        java.io.File file=new java.io.File(base,"album/"+(slide%5)+".jpg");
        Bitmap frame=BitmapFactory.decodeFile(file.getPath());
        if(frame!=null){photo.setImageBitmap(frame);photo.setVisibility(View.VISIBLE);}
        slide++;timer.postDelayed(this,30000);
    }};
    void stopAlbum(){timer.removeCallbacks(rotate);photo=null;}
    @Override protected void onPause(){visible=false;timer.removeCallbacks(rotate);super.onPause();}
    @Override protected void onResume(){super.onResume();visible=true;if(!library){refreshResume();timer.removeCallbacks(rotate);rotate.run();}}
    void refreshResume(){if(resumeLabel!=null)resumeLabel.setText(getPreferences(0).getString("label","Выберите приложение в разделе «Смотреть»"));}
    void addResumePanel(){
        LinearLayout strip=new LinearLayout(this);grid.addView(strip);
        LinearLayout panel=new LinearLayout(this);panel.setOrientation(1);panel.setGravity(Gravity.CENTER_VERTICAL);
        panel.setPadding(dp(18),0,dp(12),0);focus(panel,paint(0xff30578f,0xff17355f,12));
        panel.addView(text("Продолжить просмотр",20));resumeLabel=text("",15);panel.addView(resumeLabel);refreshResume();
        panel.setOnClickListener(v->{String pkg=getPreferences(0).getString("recent","");if(!pkg.isEmpty())open(pkg,getPreferences(0).getString("label",""));else {View next=panel.focusSearch(View.FOCUS_UP);if(next!=null)next.requestFocus();}});
        LinearLayout.LayoutParams main=new LinearLayout.LayoutParams(0,dp(82),1);main.setMargins(dp(5),dp(7),dp(5),0);strip.addView(panel,main);
        photo=new ImageView(this);photo.setScaleType(ImageView.ScaleType.FIT_CENTER);photo.setBackground(paint(0xff193864,0xff142c52,12));photo.setClipToOutline(true);photo.setVisibility(View.GONE);
        LinearLayout.LayoutParams side=new LinearLayout.LayoutParams(dp(142),dp(82));side.setMargins(dp(5),dp(7),dp(5),0);strip.addView(photo,side);
        if(visible)rotate.run();
    }
    void section(String name,int size){TextView t=text(name,size);t.setPadding(dp(6),dp(15),0,dp(6));grid.addView(t);}
    void startRow(int columns){row=new LinearLayout(this);row.setWeightSum(columns);grid.addView(row);}
    View card(String pkg,String label,String subtitle,int a,int b,boolean hero){
        LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setGravity(Gravity.BOTTOM);
        box.setPadding(dp(14),dp(10),dp(14),dp(12));focus(box,paint(a,b,15));
        try{ImageView icon=new ImageView(this);icon.setImageDrawable(getPackageManager().getApplicationIcon(pkg));
            box.addView(icon,new LinearLayout.LayoutParams(dp(hero?42:25),dp(hero?42:25)));}catch(Exception ignored){}
        if(hero)box.addView(new Space(this),new LinearLayout.LayoutParams(1,0,1));
        box.addView(text(label,hero?24:16));if(hero){TextView sub=text(subtitle,13);sub.setTextColor(0xffcedef7);box.addView(sub);}
        box.setContentDescription(label);box.setOnClickListener(v->open(pkg,label));
        LinearLayout.LayoutParams cell=new LinearLayout.LayoutParams(0,dp(hero?110:72),1);cell.setMargins(dp(5),dp(4),dp(5),dp(5));row.addView(box,cell);return box;
    }
    void showLibrary(){
        section("Остальные приложения",26);
        Set<String> hidden=new HashSet<>(Arrays.asList(FEATURE));hidden.addAll(Arrays.asList(TOOLS));
        Collections.addAll(hidden,getPackageName(),"com.yandex.tv.home","mediatek.factorymenu.ui");
        TreeMap<String,String> sorted=new TreeMap<>();
        for(String category:new String[]{Intent.CATEGORY_LEANBACK_LAUNCHER,Intent.CATEGORY_LAUNCHER}){
            for(android.content.pm.ResolveInfo item:getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(category),0)){
                String pkg=item.activityInfo.packageName;
                if(hidden.add(pkg))sorted.put(item.loadLabel(getPackageManager())+"\n"+pkg,pkg);
            }
        }
        int i=0;for(Map.Entry<String,String> e:sorted.entrySet()){if(i++%4==0)startRow(4);
            card(e.getValue(),e.getKey().split("\n")[0],"",0xff294e88,0xff163564,false);}
    }
    void open(String pkg,String label){
        Intent intent=pkg.isEmpty()?new Intent(Settings.ACTION_SETTINGS):getPackageManager().getLeanbackLaunchIntentForPackage(pkg);
        if(pkg.equals("com.yandex.tv.music"))intent=new Intent("com.yandex.tv.music.SHOW_MUSIC_PLAYER").setPackage(pkg);
        if(intent==null)intent=getPackageManager().getLaunchIntentForPackage(pkg);
        if(intent!=null){try{startActivity(intent);if(Arrays.asList(FEATURE).contains(pkg)){getPreferences(0).edit().putString("recent",pkg).putString("label",label).apply();}return;}catch(RuntimeException ignored){}}
        Toast.makeText(this,"Не удалось открыть "+label,Toast.LENGTH_SHORT).show();
    }
}



