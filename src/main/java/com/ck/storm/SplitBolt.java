package com.ck.storm;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitBolt extends BaseRichBolt{

    OutputCollector collector;

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
        
    	String line = input.getString(0);
        String[] split = line.split(" ");
        for (String word : split) {
            collector.emit(new Values(word));
        }
    }

    /**
     * 输出
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        
    	declarer.declare(new Fields("word"));
    }

}