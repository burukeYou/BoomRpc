package burukeyou.rpc.loadBalance;


import org.springframework.context.annotation.Conditional;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *      一致性哈希算法
 *
 *        1.传统hash算法  hash(key) % / 机器数 = 余数 （服务器序号）
 *          hash负载优点：
 *              相同的key可以保证相同的负载（这个特性广泛应用于缓存负载均衡领域）
 *            缺点 ：
 *              但是节点扩展后无法保证， 而且会全局影响其他之前的负载结果。但是一致性hash只会影响到小部分
 *
 *        2.原理: 事先将服务提供者列表hash映射到哈希环上(节点)，当一个请求到来对， 请求的key进行hash映射到环上
 *           然后顺时针寻找找到的第一个节点就作为负载后的服务提供者
 *
 *        3. 传统一致性哈希容易造成hash偏斜现象。比如事先将服务提供者列表全部hash映射到哈希环上(节点)的这些节点
 *           并不均匀的分割hash环，这样在实际负载的时候就违背了负载均衡的理念
 *           解决办法是给每个节点新构N个建虚拟节点，让他们在逻辑上均衡的分割Hash环
 *
 */


public class ConsistentHashStrategy   {

    /** 哈希环
     *      存放虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
    */
     private  SortedMap<Integer, String> hashRing = new TreeMap<>();

    /**
         * 虚拟节点的数目
     */
    private static final int VIRTUAL_NODES = 100;


    public  <T> ConsistentHashStrategy initHashRing(List<T> list){
        for (T ip : list) {
            // 为每个真实节点生成VIRTUAL_NODES个虚拟节点
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = ip + "&&VN" + i; // 虚拟节点名称中保存了真实节点，这样就可以找回到真实节点
                int hash = getHash(virtualNodeName);
                hashRing.put(hash, virtualNodeName);
            }
        }
        return this;
    }

    public String getServer(String key){
        int hash = getHash(key);
        System.out.println(key+ "：  key的"+hash);
        // 顺时针寻找找到的第一个节点
        SortedMap<Integer, String> sortedMap = hashRing.tailMap(hash);

        String targetNode;
        if (sortedMap.isEmpty()){
            // 如果sortedMap为null表示 hash寻找到了哈希环的尾部， 直接寻找到hash环第一个节点即可
            targetNode = hashRing.get(hashRing.firstKey());
        }else {
            targetNode = sortedMap.get(sortedMap.firstKey());
        }

        // 最后把找到的虚拟节点名称截取一下就可以获得真实节点名称
        return targetNode != null && !"".equals(targetNode) ? targetNode.substring(0,targetNode.indexOf("&&")) : null;
    }


    // FNV1_32_HASH算法
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
