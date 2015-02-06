package systemj.signals.jop;

import java.util.Hashtable;

import com.jopdesign.io.LedSwitch;
import com.jopdesign.io.LedSwitchFactory;

import systemj.interfaces.GenericSignalSender;

public class SignalLED extends GenericSignalSender {
	private LedSwitch ls = LedSwitchFactory.getLedSwitchFactory().getLedSwitch();
	
	// Shared by all SignalLED instances
	private static int LED_STATUS;
	// Mask used to determine which SW is used for this instance
	private int LED_MASK;

	@Override
	public void configure(Hashtable data) throws RuntimeException {
		if(data.containsKey("Index")){
			LED_MASK =  Integer.valueOf(((String)data.get("Index"))).intValue();
		}
		else
			throw new RuntimeException("Index not specified in XML");
	}

	@Override
	public void run() {
		if(((Boolean)buffer[0]).booleanValue() == true)
			ls.ledSwitch = LED_STATUS | LED_MASK;
		else
			ls.ledSwitch = LED_STATUS & ~LED_MASK;
	}

}
