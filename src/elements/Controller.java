package elements;

public class Controller extends Node
{
    private NIC nic;

    private static int _id = 0;
    private final int id;
    private String cid;
    private String type = "Controller";
    private String routeType = "";
    
    public Controller()
    {
        _id++;
        
        this.id = _id;
        
        this.cid = "c" + this.id;        
        
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
