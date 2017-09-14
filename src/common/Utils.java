/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;
import java.net.Inet4Address;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Utils
{
    public static String generateMAC()
    {
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte)(macAddr[0] & (byte)254);  //zeroing last 2 bytes to make it unicast and locally adminstrated

        StringBuilder sb = new StringBuilder(18);
        for(byte b : macAddr)
        {
            if(sb.length() > 0)
            {
                sb.append(":");
            }

            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();        
    }
    
    public static String IPtoString(String[] ipAddress)
    {
        String ip = ipAddress[0] + "." + ipAddress[1] 
                + "." + ipAddress[2] + "." + ipAddress[3];
        
        return ip;
    }
    
    public static String getUniqueIP()
    {
        String[] lastIp = CommonData.lastIpAddress.clone();        
        int increaseIndex = 3;
        String d = lastIp[increaseIndex];        
        int d_int = Integer.parseInt(d);
        boolean increase = false;
        
        ++d_int;
        
        if(d_int > 254)
        {
            d_int = 1;
            increase = true;
        }
        
        lastIp[increaseIndex] = "" + d_int;
        
        while(increase && increaseIndex >= 0)
        {
            --increaseIndex;
            String x = lastIp[increaseIndex];
            int x_int = Integer.parseInt(x);
            
            ++x_int;
            
            if(x_int > 254)
            {
                x_int = 1;
                increase = true;
            }
            else
            {
                increase = false;
            }
            
            lastIp[increaseIndex] = "" + x_int;
        }
        
        CommonData.lastIpAddress = lastIp.clone();
        
        return IPtoString(lastIp);
        
    }

}
