package systemj.interfaces;

import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import java.util.Hashtable;

import systemj.common.InterfaceManager;
import systemj.interfaces.GenericInterface;
import systemj.interfaces.GenericChannel;

public class CANInterface extends GenericInterface implements Runnable {
	private String ip;
	private int port;
	private String ssname; // May be used later? Not ATM
	private Object[] buffer;
	
	@Override
	public void configure(Hashtable ht) {
		if(ht.containsKey("Interface")){
/* 			// This can be used later..
		}
		if(ht.containsKey("Args")){
			String[] args = ((String)ht.get("Args")).trim().split(":");
			if(args.length != 2)
				throw new RuntimeException("Incorrect Args for TCP/IP interface : must be <IP>:<Port>");
			ip = args[0];
			port = new Integer(args[1]).intValue();
 */		}
		else
			throw new RuntimeException("Missing Args");
		
		if(ht.containsKey("SubSystem")){
			/* ssname = ((String)ht.get("SubSystem")).trim(); */
		}
	}

	@Override
	public void invokeReceivingThread() {
	//	new Thread(this).start();
	}

	@Override
	public void setup(Object[] o) {
		buffer = o;
	}

	@Override
	public boolean transmitData() {
	String channelname = (String)(buffer[0]);

    int cdnumber1 =(int)( channelname.charAt(2))-48;
    int cdnumber2 =(int)( channelname.charAt(3))-48;
    int chnumber1 =(int)( channelname.charAt(7))-48;
    int chnumber2 =(int)( channelname.charAt(8))-48;
	int channeltype=(channelname.charAt(10));
	Integer data0;
	if(channeltype=='o')
    data0 = cdnumber1*1048576+cdnumber2*65536+chnumber1*16+chnumber2+256;
	else
	data0 = cdnumber1*1048576+cdnumber2*65536+chnumber1*16+chnumber2;	

	Long value=0L;
	Integer data4 =0;
	Integer data5 =0;
	if(buffer[4]==null){
		data4=1515870810;
		data5=1515870810;
	}else{
		value = (Long)buffer[4];
		data4 = (int)(value/4294967296L);
		data5 = (int)(value-data4*4294967296L);		
	}

	int offset=chnumber1*10+chnumber2;
	
	Integer data1 =(Integer)buffer[1];
	Integer data2 =(Integer)buffer[2];	
	Integer data3 =(Integer)buffer[3];

	System.out.println("sent"+channelname+"+"+buffer[1]+"+"+buffer[2]+"+"+buffer[3]+"+"+buffer[4]);	
	
    Native.wr(data0.intValue(),Const.CAN_ADDR+1);
    Native.wr(1073741824+offset,Const.CAN_ADDR);
    Native.wr(data1.intValue(),Const.CAN_ADDR+1);
    Native.wr(1077936128+offset,Const.CAN_ADDR);
    Native.wr(data2.intValue(),Const.CAN_ADDR+1);
    Native.wr(1082130432+offset,Const.CAN_ADDR);
    Native.wr(data3.intValue(),Const.CAN_ADDR+1);
    Native.wr(1086324736+offset,Const.CAN_ADDR);
    Native.wr(data4.intValue(),Const.CAN_ADDR+1);
    Native.wr(1090519040+offset,Const.CAN_ADDR);
    Native.wr(data5.intValue(),Const.CAN_ADDR+1);
    Native.wr(1094713344+offset,Const.CAN_ADDR);	 
	return true;
	
	}

	@Override
	public void receiveData() {

 	    int status;
		int data;
		int value_high,value_low;
		long value;
        status = Native.rd(Const.CAN_ADDR+14);
		if(status!=0){
			Object[] o = new Object[5];   		 
		System.out.println("status"+status);
		data=Native.rd(Const.CAN_ADDR+15+status*4096);
		System.out.println(data);		
		String Rname= Integer.toHexString(data);
		System.out.println(Rname);		
		String Rchannelname;
	    int size=Rname.length();
	    if(size==6){		
			if (Rname.charAt(3)== '1'){
				Rchannelname="CD"+Rname.charAt(0)+Rname.charAt(1)+".CH"+Rname.charAt(4)+Rname.charAt(5)+"_o";}
			else{
				Rchannelname="CD"+Rname.charAt(0)+Rname.charAt(1)+".CH"+Rname.charAt(4)+Rname.charAt(5)+"_in";	
			}
		}else{
			if (Rname.charAt(2)== '1'){
				Rchannelname="CD0"+Rname.charAt(0)+".CH"+Rname.charAt(3)+Rname.charAt(4)+"_o";}
			else{
				Rchannelname="CD0"+Rname.charAt(0)+".CH"+Rname.charAt(3)+Rname.charAt(4)+"_in";	
			}		
		}
		 o[0]=Rchannelname;
		 System.out.println("data is "+Rchannelname);	 		 
         data = Native.rd(Const.CAN_ADDR+271+status*4096);
		 o[1]=data;
		 System.out.println("data is "+Integer.toHexString(data));	 		 
         data = Native.rd(Const.CAN_ADDR+527+status*4096);
		 o[2]=data;
		 System.out.println("data is "+Integer.toHexString(data));
         data = Native.rd(Const.CAN_ADDR+783+status*4096);
		 o[3]=data;
		 System.out.println("data is "+Integer.toHexString(data));	 
         value_high = Native.rd(Const.CAN_ADDR+1039+status*4096);
         value_low = Native.rd(Const.CAN_ADDR+1295+status*4096);
		 
		 value=value_high *4294967296L+value_low;
		 
		 o[4]=value;
/* 		 o[5]=data;
		 System.out.println("data is "+Integer.toHexString(data));	 		  */
		 super.forwardChannelData(o); 		 
		 System.out.println("data forward");	 		 
         }


	   }

	@Override
	public void run() {
/* 		// This replace receive data for TCP case
		try {
			ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
			while(true){
				//System.out.println("Listening on "+ip+" "+port);
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Object[] o = (Object[])ois.readObject();
//				GenericChannel chan = (GenericChannel)im.getChannelInstance(((String)o[0]).trim());
//				if(chan == null)
//					throw new RuntimeException("Fatal error : Could not find channel instance "+o[0]);
//				chan.setBuffer(o);
			
				super.forwardChannelData(o);
				socket.setSoLinger(true, 0);
				socket.close();
			}
		}
		catch(RuntimeException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			System.err.println("Fatal error : Could not construct the class from receiving channel data");
		}
		catch(IOException e){
			System.err.println("Error occured in TCPIPInterface, check the TCP/IP setting in the XML Interface");
			e.printStackTrace();
		}
		finally{
			System.exit(1);
		} */

	}

}
