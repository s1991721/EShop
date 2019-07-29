package com.ljf.eshop.cache.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * Created by mr.lin on 2019/7/29
 */
public class KafkaMessageProcessor implements Runnable {

    private KafkaStream kafkaStream;

    public KafkaMessageProcessor(KafkaStream kafkaStream) {
        this.kafkaStream = kafkaStream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();

        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());
        }
    }
}
