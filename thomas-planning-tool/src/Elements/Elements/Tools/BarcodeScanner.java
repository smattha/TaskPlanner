package Elements.Tools;


import java.util.Vector;

public class BarcodeScanner extends ThomasTool {

  private Integer weight;
  
  private String type;

  public Vector  myTools;
  
  
  public Boolean isCompatible(ThomasTool t1) {
	  
	  if(ToolType=="BarcodeScanner")
	  {
		  
	  
		if (((BarcodeScanner) t1).type==this.type) {
			return true;
		} else {
			return false;
		}
	  }
	  else 
	  {
		  return false;
	  }
	}

	public BarcodeScanner(String type) {

		this.type = type;
		ToolType="BarcodeScanner";
	}

}


