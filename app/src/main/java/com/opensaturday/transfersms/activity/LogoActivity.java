package com.opensaturday.transfersms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.opensaturday.transfersms.R;
import com.opensaturday.transfersms.aaa.StackOverflowAPI;
import com.opensaturday.transfersms.template.Question;
import com.opensaturday.transfersms.template.StackOverflowQuestions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

public class LogoActivity extends AppCompatActivity implements Callback<StackOverflowQuestions> {

    private static final String TAG = LogoActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // prepare call in Retrofit 2.0
        StackOverflowAPI stackOverflowAPI = retrofit.create(StackOverflowAPI.class);

        Call<StackOverflowQuestions> call = stackOverflowAPI.loadQuestions("android");
        //asynchronous call
        call.enqueue(LogoActivity.this);



    }

    @Override
    public void onResponse(Call<StackOverflowQuestions> call, Response<StackOverflowQuestions> response) {
        if(response.isSuccessful()) {
            Observable<String> simpleObservable =
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            subscriber.onNext("retrofit reponse successful !!");
                            subscriber.onCompleted();
                        }
                    });
            simpleObservable
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            startActivity(new Intent(LogoActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "retrofit error!!",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String text) {
                            Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    @Override
    public void onFailure(Call<StackOverflowQuestions> call, Throwable t) {

    }
}
