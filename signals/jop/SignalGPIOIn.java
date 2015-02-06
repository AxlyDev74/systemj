package systemj.signals.jop;

import java.util.Hashtable;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

import systemj.interfaces.GenericSignalReceiver;

public class SignalGPIOIn extends GenericSignalReceiver {
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
	public synchronized void getBuffer(Object[] obj) {
		if((Native.rd(Const.PIO_ADDRESS) & INDEX) == 0){
			obj[0] = Boolean.FALSE;
		}
		else{
			obj[0] = Boolean.TRUE;
		}
	}
	
	@Override
	public void run() {}
	
	public SignalGPIOIn() { super();}

}
