package com.jojo.socket.pool;

import com.jojo.socket.mina.MinaClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MinaClientPoolObejctFactory implements PooledObjectFactory<MinaClient> {
    @Override
    public PooledObject<MinaClient> makeObject() throws Exception {
        MinaClient minaClient = new MinaClient();
        return new DefaultPooledObject<>(minaClient);
    }

    @Override
    public void destroyObject(PooledObject<MinaClient> p) throws Exception {
        MinaClient minaClient = p.getObject();
        minaClient.close();
    }

    @Override
    public boolean validateObject(PooledObject<MinaClient> p) {
        MinaClient minaClient = p.getObject();
        return minaClient.isActive();
    }

    @Override
    public void activateObject(PooledObject<MinaClient> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<MinaClient> p) throws Exception {

    }
}
