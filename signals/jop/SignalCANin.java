package systemj.signals.jop;
import java.util.Vector;
import java.util.Hashtable;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

import systemj.interfaces.GenericSignalReceiver;
public class SignalCANin extends GenericSignalReceiver{
	
	
	private int Received_data;
    private int offset;
	/**
	 * This method is used internally which returns SystemJ signal implemented in Java object.
	 */
	@Override
	public synchronized void getBuffer(Object[] obj) {
		Received_data<=Native.rd(Const.CAN_ADDR+offset);
		if (!Received_data)
			{obj[0] = Boolean.TRUE;
			obj[1] = Received_data;}
		else
			obj[0] = Boolean.FALSE;
	}

	/**
	 * Method which initialize this receiver instance
	 * @param data Hashtable object which contains parsed data from XML
	 * @throws RuntimeException - When there is an error occured during configuration
	 */
	@Override
	public void configure(Hashtable data) throws RuntimeException {
		if(data.containsKey("Index")){
			offset =  Integer.valueOf(((String)data.get("Index"))).intValue();
		}
		else
			throw new RuntimeException("Index not specified in XML");
	}
	/**
	 * Since getting a status from switch is non-blocking, this method is not needed. </br>
	 * Instead write user-defined code in getBuffer()
	 * @author Heejong Park
	 */
	@Override
	public void run() {

	}
	
	public SignalCANin(){
		super();
	}

}
