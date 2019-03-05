package com.example.leoneyi.game_animals;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {
    ImageView cards[][];
    int cardsState[][];//0表示未知，1表示选定，2表示正确。
    int cardsContent[][];//
    int card_x=0; //表示选定的卡的行序数
    int card_y=0; //表示选定的卡的列序数
    int lockNum=0;//表示当前选定的卡的数量。最多选两张。
    int unlockTime=2;
    int gameTime=90;
    int cardsPair[];
    Timer timer;
    Handler handler;
    int myScore=0;
    int mode=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //实例化监听类
        CardsListener cardsListener=new CardsListener();
        final TextView timeLeft=(TextView)findViewById(R.id.textView_time);

        timer=new Timer();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    if(gameTime==0){
                        //创建一个Intent类，转到新的活动页面
                        // Intent intent=new Intent(PlayActivity.this,MainActivity.class);
                        //setResult(RESULT_OK,intent);
                        mode=2;
                        reSetMainActivity(2);
                        //startActivity(intent);
                    }
                    if(gameTime>=1&&mode==0)
                        gameTime-=1;
                    timeLeft.setText(Integer.toString(gameTime));
                    if(lockNum==2&&unlockTime!=0){//unlock倒计时三秒
                        unlockTime-=1;
                    }
                    else if(lockNum==2&&unlockTime==0){
                        setCards();
                    }
                }
                super.handleMessage(msg);
            }
        };
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        };
        //第二个参数：延迟多少毫秒第一次运行run（），第三个参数:以后每次运行run()时隔多少毫秒
        timer.schedule(task, 1000,1000);
        Button btn1=(Button)findViewById(R.id.button_re) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        Button btn2=(Button)findViewById(R.id.button_exit) ;
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlayActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //初始化这些卡片图片和他们的状态数组
        cards=new ImageView[4][4];
        cardsState=new int[4][4];
        cardsContent =new int[4][4];
        cardsPair=new int[2];
        cardsPair[0]=0;
        cardsPair[1]=0;


        for(int i=0;i<4;i++){
            cardsState[0][i]=0;
            cardsState[1][i]=0;
            cardsState[2][i]=0;
            cardsState[3][i]=0;
            cardsContent[0][i]=0;
            cardsContent[1][i]=0;
            cardsContent[2][i]=0;
            cardsContent[3][i]=0;
        }
        Random rand=new Random();
        for(int i=1;i<=8;i++){
            int x=rand.nextInt(4);
            int y= rand.nextInt(4);
            while(cardsContent[x][y]!=0){
                 x=rand.nextInt(4);
                 y= rand.nextInt(4);
            }
            cardsContent[x][y]=i;
            x=rand.nextInt(4);
            y= rand.nextInt(4);
            while(cardsContent[x][y]!=0){
                x=rand.nextInt(4);
                y= rand.nextInt(4);
            }
            cardsContent[x][y]=i;
        }
        /*for(int m=0;m<4;m++){
            for(int n=0;n<4;n++){
                Log.i("TAG:",Integer.toString(cardsContent[m][n]));
            }
        }*/

        //为这些卡片映射到设计中的16张卡片
        cards[0][0]=(ImageView)findViewById(R.id.card_1_1);
        cards[0][1]=(ImageView)findViewById(R.id.card_1_2);
        cards[0][2]=(ImageView)findViewById(R.id.card_1_3);
        cards[0][3]=(ImageView)findViewById(R.id.card_1_4);
        cards[1][0]=(ImageView)findViewById(R.id.card_2_1);
        cards[1][1]=(ImageView)findViewById(R.id.card_2_2);
        cards[1][2]=(ImageView)findViewById(R.id.card_2_3);
        cards[1][3]=(ImageView)findViewById(R.id.card_2_4);
        cards[2][0]=(ImageView)findViewById(R.id.card_3_1);
        cards[2][1]=(ImageView)findViewById(R.id.card_3_2);
        cards[2][2]=(ImageView)findViewById(R.id.card_3_3);
        cards[2][3]=(ImageView)findViewById(R.id.card_3_4);
        cards[3][0]=(ImageView)findViewById(R.id.card_4_1);
        cards[3][1]=(ImageView)findViewById(R.id.card_4_2);
        cards[3][2]=(ImageView)findViewById(R.id.card_4_3);
        cards[3][3]=(ImageView)findViewById(R.id.card_4_4);


        for(int m=0;m<4;m++){
            for(int n=0;n<4;n++){
                cards[m][n].setOnClickListener(cardsListener);
            }
        }

    }

    class CardsListener implements View.OnClickListener{
        @Override
        public  void onClick(View view){
            int tag=Integer.valueOf((String) view.getTag());//根据tag值判断是哪张卡被选中了。
            if(lockNum==2)
                return;
            card_x=tag/10;
            card_y=tag%10;
            if(cardsState[card_x-1][card_y-1]==0){//如果当前卡未被选定
                if(lockNum==0)
                    cardsPair[0]=tag;
                else
                    cardsPair[1]=tag;

                    lockNum+=1;
                    cardsState[card_x - 1][card_y - 1] = 1;
                    cards[card_x - 1][card_y - 1].setImageResource(R.drawable.card_2);


            }
            if(lockNum==2){//如果已经选了两张
                setCards();//unlock cards

            }
        }
    }
    public void setCards(){
        if(lockNum==2&&unlockTime!=0){//unlock cards

            for(int m=0;m<4;m++){
                for(int n=0;n<4;n++){
                    if(cardsState[m][n]==1){
                        //unlock cards with its animal
                        switch (cardsContent[m][n]){
                            case 1:
                                cards[m][n].setImageResource(R.drawable.bear);
                                break;
                            case 2:
                                cards[m][n].setImageResource(R.drawable.bird);
                                break;
                            case 3:
                                cards[m][n].setImageResource(R.drawable.cat);
                                break;
                            case 4:
                                cards[m][n].setImageResource(R.drawable.elephant);
                                break;
                            case 5:
                                cards[m][n].setImageResource(R.drawable.giraffe);
                                break;
                            case 6:
                                cards[m][n].setImageResource(R.drawable.kangaroo);
                                break;
                            case 7:
                                cards[m][n].setImageResource(R.drawable.leo);
                                break;
                            case 8:
                                cards[m][n].setImageResource(R.drawable.lion);
                                break;

                        }
                        unlockTime=2;
                    }
                }
            }
        }
        else if(lockNum==2&&unlockTime==0){//lock cards
            for(int m=0;m<4;m++){
                for(int n=0;n<4;n++){
                    if(cardsState[m][n]==1){
                        cardsState[m][n]=0;
                        if(cardsContent[cardsPair[0]/10-1][cardsPair[0]%10-1]!=cardsContent[cardsPair[1]/10-1][cardsPair[1]%10-1]) {//如果选择的两张卡不相等
                            cards[m][n].setImageResource(R.drawable.card_1);
                            myScore-=10;
                        }
                        else{
                            cards[m][n].setImageDrawable(null);
                            cardsState[m][n]=2;
                            myScore+=50;
                        }
                        lockNum=0;
                        unlockTime=2;
                        TextView scoreGet;
                        scoreGet=(TextView)findViewById(R.id.textView_score);
                        scoreGet.setText(Integer.toString(myScore));

                    }
                }
            }
            cardsPair[0]=0;
            cardsPair[1]=0;
        }
        int check=0;
        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 4; n++) {
                if(cardsState[m][n]!=2)
                    check=1;
            }
        }
        if(check==0) {
            mode=1;
            reSetMainActivity(1);
        }

    }
    public void reSetMainActivity(int mode){
        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 4; n++) {
                cards[m][n].setImageDrawable(null);
            }
        }
        TextView newText = (TextView) findViewById(R.id.mytitle);
        if(mode==1) {
            newText.setText("YOU WIN");
        }
        if(mode==2){
            newText.setText("YOU Failed");
        }

    }
}
