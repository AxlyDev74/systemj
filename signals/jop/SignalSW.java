package systemj.signals.jop;
import java.util.Vector;
import java.util.Hashtable;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

import systemj.interfaces.GenericSignalReceiver;

public class SignalSW extends GenericSignalReceiver{
	
	private int CAN_DATA;
	
	// Mask used to determine which SW is used for this instance
	private int ADDR_OFFSET;

	/**
	 * This method is used internally which returns SystemJ signal implemented in Java object.
	 */
	@Override
	public synchronized void getBuffer(Object[] obj) {
		CAN_DATA = Native.rd(Const.CAN_ADDR+ADDR_OFFSET);
		if ((CAN_DATA) == 0)
			obj[0] = Boolean.FALSE;
		else
			{obj[0] = Boolean.TRUE;
			 obj[1] = CAN_DATA;
	        }
	}

	/**
	 * Method which initialize this receiver instance
	 * @param data Hashtable object which contains parsed data from XML
	 * @throws RuntimeException - When there is an error occured during configuration
	 */
	@Override
	public void configure(Hashtable data) throws RuntimeException {
		if(data.containsKey("Index")){
			ADDR_OFFSET =  Integer.valueOf(((String)data.get("Index"))).intValue();
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
	
	public SignalSW(){
		super();
	}

}
