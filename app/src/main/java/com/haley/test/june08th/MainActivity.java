package com.haley.test.june08th;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import android.view.View;


// 네트워크 패키지
import java.net.*;
import java.io.*;


public class MainActivity extends Activity {

    // 1. 전역 변수
    private TextView mShowMessageTv;
    private EditText mInputMessageEt;
    private Button mSendMeassageBt;

    // 소켓 서버 아이피 주소는 상수로 보관
    public static final String SERVER_IP_ADDR = "10.0.2.2";

    // 소켓 서버 포트 번호 상수로 보관
    public static final int SERVER_PORT_NO = 50000;

    // 소켓 서버에 연결할 때 사용할 소켓 변수
    private Socket mSocket;

    // 사용자가 입력한 메세지를 임시 보관할 변수
    private String mStrTempMsg;

    // 내부 클래스 만들기 : Thread 클래스를 상속받는 자식 클래스
    class MyThread extends Thread {

        // run() 함수 재정의
        @Override
        public void run() {

            myLog("run()");
            //super.run();
            // 1. 네트워크 관련 명령문 작성

            // 2. 필요한 경우, 아래에 있는 handleMessage() 실행
            /* -> mHandler 변수가 갖고 있는 sendEmptyMessage() 함수 실행 시 handleMessage()가 실행됨
                mHandler.sendEmptyMessage(정수값);
                이 정수 값을 handleMessage에서 사용한 조건문의 정수와 일치 시켜야 함
            */

            // 0.5초 동안 현재 일을 하고 있는 스레드를 멈추기
        /*try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            shortToast("예외상황 발생, 원인은 : " + e.getMessage());
        }*/
            //sleepThread(500);

            // Socket 클래스를 사용해서 서버 소켓에 접속하는 명령문
            try {
                mSocket = new Socket(SERVER_IP_ADDR, SERVER_PORT_NO);
                myLog("소켓 확인");
                mStrTempMsg = "소켓 서버에 접속 성공";
                myLog("소켓 서버에 접속 성공");

            } catch(IOException e) {
                // e.printStackTrace();
                mStrTempMsg = "소켓 서버에 접속 실패";
                myLog("소켓 서버에 접속 실패");
            } finally {
                mHandler.sendEmptyMessage(0);
            }

        } // end of run()
    } // end of MyThread

    // Handler 클래스를 사용해서 handleMessage() 함수를 재정의할 변수 선언
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 0) {

                // xml에서 만든 변수들을 사용
                // mShowMessageTv.setText("어쩌구저쩌구");
                mShowMessageTv.setText(mStrTempMsg);

            }


        } // end of handleMessage
    }; // end of mHandler -> 핸들러를 스레드 안으로 넣는건가?????


    // 서버에서 사용자가 입력한 메세지를 가져와 화면에 출력해주는 스레드 클래스
    class MyThread2 extends Thread {
        @Override
        public void run() {

            mHandler2.sendEmptyMessage(0);


        } // end of run()

    } // end of MyThread2

    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myLog("mHandler2");

            if(msg.what == 0) {
                mShowMessageTv.append(mStrTempMsg);
            }
        } // end of handleMessage()
    }; // end of mHandler2


    // 위에서 만들 스레드 클래스들의 변수 선언
    private MyThread myThread;
    private MyThread2 myThread2;

    // 2. 재정의 함수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLog("onCreate() 실행");

        this.mShowMessageTv = (TextView)this.findViewById(R.id.showMessageTv);
        this.mInputMessageEt = (EditText)this.findViewById(R.id.inputMessageEt);
        this.mSendMeassageBt = (Button)this.findViewById(R.id.sendMeassageBt);

        // 자동으로 소켓 서버에 접속하는 스레드 객체 실행 명령문
        myThread = new MyThread();
        myThread.start();
        myLog("myThread 실행?");

        // 버튼 클릭 이벤트
        this.mSendMeassageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLog("사용자가 전송 버튼 클릭");

                String temp = mInputMessageEt.getText().toString();

                if(temp == null || temp.trim().length() == 0) {
                    shortToast("메세지를 입력하세요.");
                } else {
                    shortToast("전송한 메세지 : " + temp);

                    String result = "";
                    result += temp;

                    mStrTempMsg = result;

                    // 스레드 클래스에서 화면에 출력하는 부분 작성
                    myThread2 = new MyThread2();
                    myThread2.start();
                }

            }
        });

    } // end of onCreat();


    // 3.사용자 정의 함수

    private void shortToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }

    private void myLog(String message) {

        Log.v("**MainActivity**", message);
    }

    private void sleepThread(int miliSecond) {

        try {
            Thread.sleep(miliSecond);
        } catch(InterruptedException e) {
            shortToast("예외상황 발생, 원인은 : " + e.getMessage());
        }
    }


} // end of MainActivity
