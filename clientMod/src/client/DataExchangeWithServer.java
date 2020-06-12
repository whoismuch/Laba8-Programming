package client;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DataExchangeWithServer {
    private SocketChannel outcomingchannel;


    public DataExchangeWithServer (SocketChannel outcomingchannel) {
        this.outcomingchannel = outcomingchannel;
    }

    public void sendToServer (Object object) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);

        byte[] outcoming = baos.toByteArray( );

        ByteBuffer byteBuffer = ByteBuffer.wrap(outcoming);

        boolean iAmIn = false;
        while (!iAmIn) {
            iAmIn = sendCycle(byteBuffer);
        }

        byteBuffer.clear( );
        baos.flush( );
        oos.flush( );
        oos.close();
        baos.close();

    }

    public boolean sendCycle (ByteBuffer byteBuffer) throws IOException {
        boolean in = false;
        while ((outcomingchannel.write(byteBuffer)) > 0) {
            in = true;
        }
        return in;
    }


    public Object getFromServer ( ) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream( );
            ByteBuffer byteBuffer = ByteBuffer.allocate(5000);
            int n = 0;
            while ((n = outcomingchannel.read(byteBuffer)) > 0) {
                byteBuffer.flip( );
                baos.write(byteBuffer.array( ), 0, n);
            }
            ByteArrayInputStream bios = new ByteArrayInputStream(baos.toByteArray( ));
            ObjectInputStream ois = new ObjectInputStream(bios);
            Object o = ois.readObject( );
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace( );
            return null;
        } catch (EOFException e) {
            e.printStackTrace( );
            return null;
        }
    }
}
