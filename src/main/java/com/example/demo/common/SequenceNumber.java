package com.example.demo.common;

import com.example.demo.exception.SystemException;
import com.example.demo.service.impl.SequenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 流水号生成
 *
 * @author raining_heavily
 * @date 2020/4/18 16:37
 **/
@Component
public class SequenceNumber {

    private SequenceNumber() { }

    @Autowired
    private SequenceServiceImpl sequenceService;

    @PostConstruct
    public void init() {
        //解决在单例中直接使用@Autowired注入对象为null
        SequenceNumberHelper.instance = this;
        SequenceNumberHelper.instance.sequenceService = this.sequenceService;
    }

    private static class SequenceNumberHelper {
        static SequenceNumber instance = new SequenceNumber();
    }

    public static SequenceNumber getInstance() {
        return SequenceNumberHelper.instance;
//        return instance;
    }

    public synchronized String getNextSequence(String type) throws SystemException {
        return sequenceService.getNextSequences(type,1)[0];
    }

    public synchronized String[] getNextSequences(String type,int num) throws SystemException {
        return sequenceService.getNextSequences(type, num);
    }

}
