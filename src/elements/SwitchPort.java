/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

/**
 *
 * @author Daniel
 */
public class SwitchPort extends NIC
{
    private int port;    
    
    public SwitchPort(int port, String mac, String ip)
    {
        super(mac, ip);
        this.port = port;
    }


    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public String getMAC()
    {
        return super.getMac();
    }
    
    public String getIP()
    {
        return super.getIp();        
    }
    
    public void setMAC(String mac)
    {
        super.setMac(mac);
    }

    public void setIP(String ip)
    {
        super.setIp(ip);
    }
}
