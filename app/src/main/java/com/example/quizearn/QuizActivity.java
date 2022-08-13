package com.example.quizearn;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizearn.databinding.ActivityQuizBinding;

import java.util.ArrayList;
import java.util.Optional;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question>questions;
    int index = 0;
    int countClick = 0;
    int checkClick = 0;
    Question question;
    CountDownTimer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();
        questions.add(new Question("What is Earth?","Planet","Sun","Human","Car","Planet"));
        questions.add(new Question("What is Samosa?","Planet","Food","Human","Car","Food"));

        resetTimer();
        setNextQuestion();

    }

    void resetTimer(){
         timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText((int) (millisUntilFinished/1000));
            }

             @Override
             public void onFinish() {
//                 Toast.makeText(QuizActivity.this, "Your time has End", Toast.LENGTH_SHORT).show();
                 Log.i(TAG,"Time finished");
            }
        };
    }

    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }
    void setNextQuestion(){

        if(timer != null){
            timer.cancel();
        }
//        Log.d("aman","Hey Error Found");
        timer.start(); //here have a error

        if(index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),questions.size())); //show how many question are remaining.

             question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }


    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }else{
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }
    void resetOption(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }
    public void onClick(View view){

        switch(view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:

                if(countClick == index){  //if one option is selected then other option is not allowed to check.

                    if(timer != null) timer.cancel(); //timer stop

                    TextView selected1 = (TextView) view;
                    checkAnswer(selected1);
                    countClick++;
                    checkClick = 1; //just check any one option is selected.?
                }
                break;

            case R.id.Nextbtn:
                if(index < questions.size()) {
                    index++;

                    if(index != questions.size())
                      resetOption();

                    if(checkClick == 0)
                        countClick++;

                    setNextQuestion();
                }
                else{
                    Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}