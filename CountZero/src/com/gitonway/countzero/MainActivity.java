package com.gitonway.countzero;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gitonway.countzero.TimelyView.FinishListener;




public class MainActivity extends Activity implements OnClickListener {
    private             TimelyView     timelyView     = null;
    private  			Button		   mButton		  = null;	
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timelyView = (TimelyView) findViewById(R.id.textView1);
        mButton	   = (Button) findViewById(R.id.startAnim);
        mButton.setOnClickListener(this);
        
        
    }

	@Override
	public void onClick(View v) {
		if (timelyView.getFromNumber()==0) {
			timelyView.setFromNumber(9);
		}
		
		
		timelyView.setOnFinish(new FinishListener() {
			
			@Override
			public void onCountDownFinish() {
				
				Toast.makeText(getApplicationContext(), "countdown to the end", Toast.LENGTH_SHORT).show();
			}
		});
		timelyView.start(this);
	}
    
    
    


}
