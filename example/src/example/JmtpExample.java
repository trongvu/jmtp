/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;
import be.derycke.pieter.com.COMException;
import java.io.File;
import java.math.BigInteger;
import jmtp.PortableDevice;
import jmtp.PortableDeviceFolderObject;
import jmtp.PortableDeviceManager;
import jmtp.PortableDeviceObject;
import jmtp.PortableDeviceStorageObject;
import jmtp.PortableDeviceToHostImpl32;
/**
 *
 * @author vungo_000
 */
public class JmtpExample {

    /**
     * @param args the command line arguments
     */
    static {
        System.loadLibrary("jmtp");
    }
    public static void main(String[] args) {
        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice device = manager.getDevices()[0]; 
        PortableDeviceFolderObject targetFolder = null;
        // Connect to my mp3-player
        device.open();
        System.out.println(device.getModel());
        System.out.println(device.getSerialNumber());
        System.out.println("---------------");

        for (PortableDeviceObject object : device.getRootObjects()) 
        {
            // If the object is a storage object
            if (object instanceof PortableDeviceStorageObject) 
            {
                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;
                targetFolder = storage.createFolderObject("TestMtp");
                copyFileFromComputerToDeviceFolder(targetFolder, "C:\\Users\\vungo_000\\Desktop\\get_posts.py");
                PortableDeviceObject[] folderFiles = targetFolder.getChildObjects();
                for (PortableDeviceObject pDO : folderFiles) {
                    
                    copyFileFromDeviceToComputerFolder(pDO, device, "C:\\Users\\vungo_000\\Desktop\\jmtp");
                }
            }
        }
        manager.getDevices()[0].close();

    }
    
    private static void copyFileFromDeviceToComputerFolder(PortableDeviceObject pDO, PortableDevice device, String destFolder)
    {
        PortableDeviceToHostImpl32 copy = new PortableDeviceToHostImpl32();
        try {
            copy.copyFromPortableDeviceToHost(pDO.getID(), destFolder, device);
        } catch (COMException ex) {
            ex.printStackTrace();
        }

    }

    private static void copyFileFromComputerToDeviceFolder(PortableDeviceFolderObject targetFolder, String fileToCopy) 
    {
        BigInteger bigInteger1 = new BigInteger("123456789");
        File file = new File(fileToCopy);
        try {
            targetFolder.addAudioObject(file, "jj", "jj", bigInteger1);
        } catch (Exception e) {
            System.out.println("Exception e = " + e);
        }
    }
}
