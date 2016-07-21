/**
 * 
 */
package ca.sait.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * 
 * @author Chris Elias
 */
public class UUIDUtils {

    /**
     * 
     * @param uuid
     * @return
     */
    public static byte[] getIdAsByte(UUID uuid) {
        final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * 
     * @param b
     * @return
     */
    public static UUID fromByte(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        
        return new UUID(firstLong, secondLong);
    }
    
    /**
     * 
     * @param uuid
     * @return
     */
    public static UUID fromString(String uuid) {
        return UUID.fromString(uuid);
    }
    
    
    public static void main(String [] args) {
        final UUID uuid = UUID.randomUUID();
        System.out.println("UUID: " + uuid.toString());
        final byte [] b = getIdAsByte(uuid);
        final UUID newUuuid = fromByte(b);
        System.out.println("New UUID: " + newUuuid.toString());
        System.out.println("String UUID: " + fromString(uuid.toString()).toString());
        
    }
}
