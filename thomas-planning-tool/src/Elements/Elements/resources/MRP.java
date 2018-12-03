package Elements.resources;

import java.math.BigInteger;
import java.util.Vector;

public class MRP extends Resources {


public BigInteger id;

private Arm1 arm1;

private Arm2 arm2;

public MRP(BigInteger id1, String name)
{
	  this.id=id1;
	  this.name=name;
	  arm1=null;
	  arm2=null;
	  gererateResource();
}

public MRP(BigInteger id1, String name, Arm1 arm1,Arm2 arm2)
{
	
	  this.id=id1;
	  this.name=name;
	  this.arm1=arm1;
	  this.arm2=arm2;
	  gererateResource();
}

public void setARM1(Arm1 arm1)
{
	this.arm1=arm1;
	
	if(arm2!=null)
	{
		arm2.connectedArm(arm1);	
		arm1.connectedArm(arm2);
	}
	
}


public void setARM2(Arm2 arm2)
{
	this.arm2=arm2;
	
	if(arm1!=null)
	{
		arm2.connectedArm(arm1);	
		arm1.connectedArm(arm2);
	}
	
}


public Arm1 getARM1(Arm1 arm1)
{
	return this.arm1;
}


public Arm2 getARM2(Arm2 arm2)
{
	return this.arm2;
}
}