package swantron.project.rbg;

import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class IOIORBG extends AbstractIOIOActivity {
	private TextView textView1_;
	private TextView textView2_;
	private TextView textView3_;
	private SeekBar seekBar1_;
	private SeekBar seekBar2_;
	private SeekBar seekBar3_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView1_ = (TextView)findViewById(R.id.TextView1);
        textView2_ = (TextView)findViewById(R.id.TextView2);
        textView3_ = (TextView)findViewById(R.id.TextView3);
        seekBar1_ = (SeekBar)findViewById(R.id.SeekBar1);
        seekBar2_ = (SeekBar)findViewById(R.id.SeekBar2);
        seekBar3_ = (SeekBar)findViewById(R.id.SeekBar3);
        enableUi(false);
    }
	
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {

		private PwmOutput pwmOutput1_;
		private PwmOutput pwmOutput2_;
		private PwmOutput pwmOutput3_;

		
		public void setup() throws ConnectionLostException {
			try {
				pwmOutput1_ = ioio_.openPwmOutput(10, 100);
				pwmOutput2_ = ioio_.openPwmOutput(11, 100);
				pwmOutput3_ = ioio_.openPwmOutput(12, 100);
				
				enableUi(true);
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}
		
		public void loop() throws ConnectionLostException {
			try {
				setText1(Float.toString(seekBar1_.getProgress() * 2));
				setText2(Float.toString(seekBar2_.getProgress() * 2));
				setText3(Float.toString(seekBar3_.getProgress() * 2));
							
				pwmOutput1_.setPulseWidth(seekBar1_.getProgress() * 2);
				pwmOutput2_.setPulseWidth(seekBar2_.getProgress() * 2);
				pwmOutput3_.setPulseWidth(seekBar3_.getProgress() * 2);

				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				seekBar1_.setEnabled(enable);
				seekBar2_.setEnabled(enable);
				seekBar3_.setEnabled(enable);
			}
		});
	}
	
	private void setText1 (final String str1) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView1_.setText(str1);

			}
		});
	}
	
	private void setText2 (final String str2) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView2_.setText(str2);

			}
		});
	}
	
	private void setText3 (final String str3) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView3_.setText(str3);

			}
		});
	}	
	
	
}
