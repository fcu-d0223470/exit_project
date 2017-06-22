package jacky.androidprogram;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by user on 2017/6/22.
 */

public class test extends Thread {
    String msg;
    String ip;
    Activity at;
    TextView tv;
    int port;
    int i,a;

    private MediaPlayer player;
    test(Activity at,String msg,TextView tv,String ip,int port){
        super();
        this.at=at;
        this.tv=tv;
        this.msg=msg;
        this.ip=ip;
        this.port=port;
    }

    public void run(){
        try {
            Socket s;
            InetAddress serverIp = InetAddress.getByName(ip);
            s = new Socket(serverIp, port);
            try{
                OutputStream os = s.getOutputStream();
                PrintWriter pwriter = new PrintWriter(os,true);
                pwriter.println(msg);

                InputStream is = s.getInputStream();
                InputStreamReader reader=new InputStreamReader(is);
                BufferedReader bufferedReader=new BufferedReader(reader);
                String result= bufferedReader.readLine();
                s.close();
                oc(at,result,true);

            }catch (Exception e){
                oc(at,"無法連線:"+ip,false);
            }


        }catch (Exception e) {
            oc(at,"socket failed:"+ip+":"+port+"\n 1.0.6 ",false);
        }


    }
    public void oc(Activity at, final String r, final boolean isMusic){

        Message msg = Message.obtain();
        msg.obj = "hello";


    }
    private MediaPlayer.OnCompletionListener comL =new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try{
                player.stop();
                player.prepare();
            }catch (Exception e){
            }
        }
    };
}
