package com.jojo.socket;

import com.jojo.socket.mina.MinaClient;
import com.jojo.socket.pool.MinaClientPoolObejctFactory;
import com.jojo.socket.request.BaseRequest;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ClientPoolDemo {

    public static void main(String[] args) throws Exception {
        MinaClientPoolObejctFactory minaClientPoolObejctFactory = new MinaClientPoolObejctFactory();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(4);

        // 对象池
        ObjectPool<MinaClient> objectPool = new GenericObjectPool<>(minaClientPoolObejctFactory, poolConfig);
        // 拿出一个对象
        MinaClient minaClient = objectPool.borrowObject();

        BaseRequest request = new BaseRequest();
        request.setMessage("1720210223    231011|AOST001SSLSW|AB001T200270647|AC");

        minaClient.writeAndWaitResponse(request);

        // 归还对象
        objectPool.returnObject(minaClient);

        // 对象池销毁
        objectPool.close();
    }
}
