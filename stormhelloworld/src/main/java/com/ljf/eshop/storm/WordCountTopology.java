package com.ljf.eshop.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mr.lin on 2019/8/2
 */
public class WordCountTopology {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTopology.class);

    public static class RandomSentenceSpout extends BaseRichSpout {

        private SpoutOutputCollector collector;
        private Random random;

        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.collector = spoutOutputCollector;
            this.random = new Random();
        }

        public void nextTuple() {
            Utils.sleep(100);
            String[] sentences = new String[]{"the cow jumped over the moon",
                    "an apple a day keeps the doctor away",
                    "four score and seven years ago",
                    "snow white and the seven dwarfs",
                    "i am at two with nature"};

            String sentence = sentences[random.nextInt(sentences.length)];

            LOGGER.info("【发射句子】sentence = " + sentence);

            collector.emit(new Values(sentence));
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("sentence"));
        }
    }

    public static class SplitSentence extends BaseRichBolt {

        private OutputCollector collector;

        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.collector = outputCollector;
        }

        public void execute(Tuple tuple) {
            String sentence = tuple.getStringByField("sentence");

            String[] words = sentence.split(" ");

            for (String word : words) {
                collector.emit(new Values(word));
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word"));
        }
    }

    public static class WordCount extends BaseRichBolt {

        private OutputCollector collector;
        private Map<String, Long> wordCounts = new HashMap<String, Long>();

        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.collector = outputCollector;
        }

        public void execute(Tuple tuple) {
            String word = tuple.getStringByField("word");

            Long count = wordCounts.get(word);
            if (count == null) {
                count = 0L;
            }
            count++;
            wordCounts.put(word, count);

            LOGGER.info("【单词计数】" + word + "出现的次数是" + count);

            collector.emit(new Values(word, count));
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word", "count"));
        }
    }

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("RandomSentence", new RandomSentenceSpout(), 2);

        builder.setBolt("SplitSentence", new SplitSentence(), 5)
                .setNumTasks(10)
                .shuffleGrouping("RandomSentence");

        builder.setBolt("WordCount", new WordCount(), 10)
                .setNumTasks(20)
                .fieldsGrouping("SplitSentence", new Fields("word"));

        Config config = new Config();

        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], config, builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        } else {
            config.setMaxTaskParallelism(20);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("WordCountTopology", config, builder.createTopology());

            Utils.sleep(60 * 1000);
            cluster.shutdown();
        }

    }

}
