package com.ck.storm;

import java.util.Map;

import org.apache.thrift7.TException;
import org.json.simple.JSONValue;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.Nimbus;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws AlreadyAliveException, InvalidTopologyException, TException
    {
    	//创建一个TopologyBuilder
        TopologyBuilder tb = new TopologyBuilder();
        tb.setSpout("SpoutBolt", new SpoutBolt(), 2);
        tb.setBolt("SplitBolt", new SplitBolt(), 2).shuffleGrouping("SpoutBolt");
        tb.setBolt("CountBolt", new CountBolt(), 4).fieldsGrouping("SplitBolt", new Fields("word"));
        //创建配置
        Config conf = new Config();
        //设置worker数量
        conf.setNumWorkers(2);
        //提交任务
        //集群提交
        //      StormSubmitter.submitTopology("myWordcount", conf, tb.createTopology());
        //本地提交
       // LocalCluster localCluster = new LocalCluster();
        //localCluster.submitTopology("myWordcount", conf, tb.createTopology());
      /*  try {
        	
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, tb.createTopology());
			
		} catch (AlreadyAliveException | InvalidTopologyException e) {
			e.printStackTrace();
		} */
        
        //提交到storm集群
        // 读取本地 Storm 配置文件
        Map stormConf = Utils.readStormConfig();
        stormConf.put("nimbus.host", "cluster");
        stormConf.putAll(conf);

        Nimbus.Client client = NimbusClient.getConfiguredClient(stormConf).getClient();
        String inputJar = "D:\\Users\\Jenson\\eclipse-workspace\\storm\\target\\storm-0.0.1-SNAPSHOT.jar";
        NimbusClient nimbus = new NimbusClient(stormConf, "cluster", 6627);

        // 使用 StormSubmitter 提交 jar 包
        String uploadedJarLocation = StormSubmitter.submitJar(stormConf, inputJar);
        String jsonConf = JSONValue.toJSONString(stormConf);
        nimbus.getClient().submitTopology("remotetopology", uploadedJarLocation, jsonConf, tb.createTopology());
    }
}
