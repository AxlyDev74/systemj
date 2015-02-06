package systemj.lib;

import systemj.interfaces.*;

import java.util.*;
/**
 * 01/11/2011
 * Asynchronous channel object
 * @author HeeJong Park 
 */
public class AChannel extends GenericChannel{
	private final Object LOCK = new Object();
	private int status = 0;
	public AChannel(){}
	public Object getValue(){/*synchronized(this.LOCK){*/return value;/*}*/}
	public void setValue(Object in){
		synchronized(this.LOCK){
			if(status == Integer.MAX_VALUE) 
				status = 1;
			else
				status++; 
			this.value = in;
		} 
		this.modified=true;
	}
	public Object getLock(){return LOCK;}
	public int getStatus(){return status;}
	
	// Socket communication
	public synchronized void getBuffer(){
		value = toReceive[1];
		status = ((Integer)toReceive[2]).intValue();
		incoming = false;
	}
	
	public void gethook(){
		if(init && !isLocal){
			if(incoming){
				this.getBuffer();
			}
		}
	}
	public void sethook(){
		if(init && !isLocal){
			if(this.modified){
				Object[] toSend = new Object[4];  // Creating an Object!!
				toSend[0] = PartnerName;
				toSend[1] = value;
				toSend[2] = new Integer(status); // Creating an Object!!
				if(value != null)
					toSend[3] = value;
				if(super.pushToQueue(toSend))
					this.modified = false; // This is set to false ONLY if the data is received by other side
			}
		}
	}
	public void setDistributed(){ isLocal = false;}
}
