package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Dict;
import com.example.demo.entity.Sequence;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author raining_heavily
 * @date 2020/4/18 21:06
 **/
@Service
public class SequenceServiceImpl {

    @Autowired
    private SequenceMapper sequenceMapper;

    private static int MAX_NUM = 9;

    /**
     * 生成多个流水号，最多9个
     * @param type
     * @param num
     * @return
     * @throws SystemException
     */
    @Transactional
    public String[] getNextSequences(String type, int num) throws SystemException {
        if (num > MAX_NUM || num <= 0) {
            throw new SystemException(ExceptionEnums.DATA_HANDLE_ERROR);
        }
        QueryWrapper query = new QueryWrapper<Sequence>().eq("sequence_type", type);
        Sequence sequence = sequenceMapper.selectOne(query);
        long no = sequence.getSequenceNo();
        int random = new Random().nextInt(50);
        String[] result = new String[num];
        for (int i = 0; i < num; i++) {
            result[i] = sequenceFormat(type, no);
            no += random;
        }
        sequence.setSequenceNo(no);
        sequenceMapper.update(sequence, query);
        return result;
    }

    private String sequenceFormat(String type, long no) throws SystemException {
        if (Dict.SEQUENCE_TYPE_IMAGE.equals(type)) {
            return new DecimalFormat("0000").format(no) + System.currentTimeMillis();
        } else if (Dict.SEQUENCE_TYPE_WRITING.equals(type)) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).substring(2) + new DecimalFormat("0000").format(no);
        } else {
            throw new SystemException(ExceptionEnums.DATA_HANDLE_ERROR);
        }
    }
}
