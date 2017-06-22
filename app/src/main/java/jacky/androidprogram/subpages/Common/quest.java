package jacky.androidprogram.subpages.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jacky.androidprogram.R;

public class quest extends Activity {
    String data = "";
    int str1 = 0;
    int str2 = 0;
    int str3 = 0;
    int doneflag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest);
        ImageView QuestImg = (ImageView)findViewById(R.id.Qpic);
        TextView StoryRef = (TextView)findViewById(R.id.description);
        final Button Q1 = (Button)findViewById(R.id.QB1);
        final Button Q2 = (Button)findViewById(R.id.QB2);
        final Button Q3 = (Button)findViewById(R.id.QB3);
        Button GET = (Button)findViewById(R.id.getdata);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final TextView datav = (TextView)findViewById(R.id.datas);
        Intent intext = getIntent();
        Bundle bundle = intext.getExtras();
        final String user_name = bundle.getString("user_name");
        //datav.setText(owner.toString());
        final Button done = (Button) findViewById(R.id.donef);
        GET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //parseJSON(data);
            }

        });
        Q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str1==1){
                    vibe.vibrate(100);
                    AlertDialog.Builder AD = new AlertDialog.Builder(quest.this);
                    String result = "";
                    result = "二一步道\n" +
                            "\n" +
                            "\t\t\t\t\t\t據說逢甲有一條傳說步道，已流傳20多年，一代接一代，原因已不可考，但同學們深信不疑，他有一種神祕力量，只要去走一回，將會面臨學業被二一的命運，有少數人不信邪的走了幾次，你知道……他們現在…..在哪裡嗎？";
                    AD.setTitle("");
                    AD.setMessage(result);
                    AD.show();
                }
            }
        });
        Q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str2==1){
                    vibe.vibrate(100);
                    AlertDialog.Builder AD = new AlertDialog.Builder(quest.this);
                    String result = "";
                    result = "人言大樓\n" +
                            "\n" +
                            "\t\t\t\t\t\t人言大樓完工於民國82年11月，為紀念本校高信（人言先生）董事長對本校之貢獻故命名為人言大樓，其原址為女教職員招待所。本大樓為十四層建築物，一樓設有書局、藝術中心展館，八樓以上為人文社會學院十樓為校友聯絡室及通識沙龍，二樓至七樓為教室。目前為本校重要中心地標之一。";
                    AD.setTitle("");
                    AD.setMessage(result);
                    AD.show();
                }
            }
        });
        Q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str3==1){
                    vibe.vibrate(100);
                    AlertDialog.Builder AD = new AlertDialog.Builder(quest.this);
                    String result = "";
                    result = "體育館\n" +
                            "\n" +
                            "\t\t\t\t\t\t體育館原址為學思園、涼亭、溜冰場、游泳池。為增加校內生師運動空間以及提高運動品質，體育館於民國94年10月完工，總建物有八層，設有室內跑道、標準籃球場、健身房、韻律教室、飛輪教室、游泳池等設施。體育館之建造，完全符合綠建築標準，線條流動的外觀，更是讓所有在室內室外運動的使用者在運動的同時，享有視覺的變化。";
                    AD.setTitle("");
                    AD.setMessage(result);
                    AD.show();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(100);
                AlertDialog.Builder AD = new AlertDialog.Builder(quest.this);
                String result = "";
                result = "恭喜完成任務，請至櫃檯領取1個泡芙。";
                AD.setTitle("");
                AD.setMessage(result);
                AD.show();
            }
        });
    }
}
