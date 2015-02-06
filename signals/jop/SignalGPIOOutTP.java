package systemj.signals.jop;

import java.util.Hashtable;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

import systemj.interfaces.GenericSignalSender;

public class SignalGPIOOutTP extends GenericSignalSender {

	int INDEX = 0;
	@Override
	public void configure(Hashtable data) throws RuntimeException {
		if(data.containsKey("Index")){
			INDEX = Integer.valueOf(((String)data.get("Index"))).intValue();
		}
		else
			throw new RuntimeException("Index not specified in XML");

	}

	@Override
	public void run() {
		String val = (String)super.buffer[1];
		if(val.equals("high"))
			Native.wr(INDEX, Const.PIO_ADDRESS);
		else if(val.equals("low"))
			Native.wr(~INDEX, Const.PIO_ADDRESS+1);
	}

	// TODO: Change RTS so every signal sender will execute an additional method
	// in case when signal is absent (for clearing a signal)
	//	@Override
	//	public void arun(){
	//		Native.wr(INDEX, Const.PIO_CLEAR_ADDRESS);
	//	}
}
