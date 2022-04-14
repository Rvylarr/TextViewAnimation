package dev.slarrties.textviewanimation;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintlayout);
        TextView textView = (TextView)findViewById(R.id.textview);

        Display display = getWindowManager().getDefaultDisplay();
        float height = display.getHeight(); //получаем значение о высоте дисплея
        float height_back = -height;

        TranslateAnimation do_nothing_for_text = new TranslateAnimation(0,0,0,0);
        do_nothing_for_text.setDuration(1);
        TranslateAnimation do_nothing_for_text2 = new TranslateAnimation(0,0,0,0);
        do_nothing_for_text2.setDuration(1000);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String country = Locale.getDefault().getLanguage();

                //перекрашивает текст в зависимости от локализации
                if (country.equals("ru")) {
                    textView.setTextColor(Color.parseColor("#0000FF"));
                } else if (country.equals("en")) { //английский работает с English(United states)
                    textView.setTextColor(Color.parseColor("#FF0000"));
                }

                //перемещение текста в точку нажатия на экране
                int posX = (int)event.getX();
                int posY = (int)event.getY();
                textView.setX(posX);
                textView.setY(posY);

                float deltaY = height - posY;
                float deltaYtoBack = height_back + deltaY; //высчитывает расстояние от объекта до потолка

                TranslateAnimation anim = new TranslateAnimation(0,  0, 0, deltaY);
                anim.setDuration(3000);

                TranslateAnimation anim_loop_to_up = new TranslateAnimation(0,0, deltaY, deltaYtoBack);
                anim_loop_to_up.setDuration(3000);

                TranslateAnimation anim_loop_to_down = new TranslateAnimation(0,0, deltaYtoBack, deltaY);
                anim_loop_to_down.setDuration(3000);

                TranslateAnimation do_nothing = new TranslateAnimation(0,0,0,0);

                //сброс анимации для начала нового обратного отсчета
                textView.startAnimation(do_nothing);

                new CountDownTimer(5000,1000) { // таймер ждет 5 сек. после нажатия
                    @Override
                    public void onTick(long l) { }
                    @Override
                    public void onFinish() {
                        //после 5 секунд ожидания проигрывается анимация перемещения текстового поля вниз экрана
                        textView.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) { }
                            public void onAnimationRepeat(Animation animation) { }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                textView.startAnimation(anim_loop_to_up);
                            }
                        });
                        //затем до верха
                        anim_loop_to_up.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) { }
                            public void onAnimationRepeat(Animation animation) { }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                textView.startAnimation(anim_loop_to_down);
                            }
                        });
                        //и так по кругу
                        anim_loop_to_down.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) { }
                            public void onAnimationRepeat(Animation animation) { }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                textView.startAnimation(anim_loop_to_up);
                            }
                        });
                    }
                }.start();
                return false;
            }
        });

        //обработка нажатия на текстовое поле
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.startAnimation(do_nothing_for_text);

                /*Я перепробовал очень много вариантов с остановкой движения
                текстового поля и единственным рабочим оказался вариант ниже
                */
                do_nothing_for_text.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) { }
                    public void onAnimationRepeat(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.startAnimation(do_nothing_for_text2);
                    }
                });

                do_nothing_for_text2.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) { }
                    public void onAnimationRepeat(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.startAnimation(do_nothing_for_text);
                    }
                });
            }
        });
    }
}