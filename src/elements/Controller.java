package elements;

public class Controller extends Node
{
    //private NIC nic;

    //private static int _id = 0;
    private int id;
    //private final int port = 0 + (int)(Math.random() * 65535);
    private String cid;
    private String type = "Controller";
    private String routeType = "";
    
    public Controller()
    {
       // _id++;
        
    	this.id = common.CommonData.controllerCount+1;
        
        
        this.cid = "c" + this.id;        
        //this.nic = new NIC(this.port);
    }

    /**
     * @return the cid
     */
    public String getSid()
    {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setSid(String cid)
    {
        this.cid = cid;
    }
    public int getID()
    {
        return this.id;        
    }
    public void setID(int x)
    {
        this.id = x;        
    }
    
    
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
    /**
     * method sets route type
     * @param x
     * set routeType
     */
    public void setRouteType(String x)
    {
        this.routeType = x;
    }
    /**
     * method returns routeType
     * @return routeType
     */
    public String getRouteType()
    {
        return routeType;
    }
}
