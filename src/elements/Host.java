package elements;

import java.util.ArrayList;

public class Host extends Node
{
    private ArrayList<NIC> nics;
    //private static int _id = 0;
    private final int id;
    private String hid;
    private String type = "Host";
    private String routeType = "";
    
    public Host()
    {
        //_id++;
        
        this.id = common.CommonData.hostsCount+1;
        
        this.hid = "h" + this.id;        
    }
    

    /**
     * @return the nics
     */
    public ArrayList<NIC> getNics()
    {
        return nics;
    }

    /**
     * @param nics the nics to set
     */
    public void setNics(ArrayList<NIC> nics)
    {
        this.nics = nics;
    }

    /**
     * @return the hid
     */
    public String getSid()
    {
        return hid;
    }

    /**
     * @param hid the hid to set
     */
    public void setSid(String hid)
    {
        this.hid = hid;
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
