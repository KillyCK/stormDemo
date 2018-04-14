package com.ck.storm;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class CountBolt extends BaseRichBolt{

    OutputCollector collector;
    Map<String, Integer> map = new HashMap<String, Integer>();

    /**
     * 初始化
     */
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 执行方法
     */
    public void execute(Tuple input) {
        String word = input.getString(0);
        if(map.containsKey(word)){
            Integer c = map.get(word);
            map.put(word, c+1);
        }else{
            map.put(word, 1);
        }
        //测试输出
        System.out.println("结果:"+map);
    }

    /**
     * 输出
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}