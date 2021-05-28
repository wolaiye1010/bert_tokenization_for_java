package com.bj58.hy.strategy.bert.tokenizer;

import java.util.List;

/**
 * Created by zhudongchang on 2021/5/28 10:41 上午
 */
class TokenEntity {
    private List<Integer> inputIds;
    private List<Integer> inputMask;
    private List<Integer> segmentIds;

    public TokenEntity(List<Integer> inputIds, List<Integer> inputMask, List<Integer> segmentIds){
        this.inputIds = inputIds;
        this.inputMask = inputMask;
        this.segmentIds = segmentIds;

    }

    public List<Integer> getInputIds() {
        return inputIds;
    }

    public List<Integer> getInputMask() {
        return inputMask;
    }

    public void setInputMask(List<Integer> inputMask) {
        this.inputMask = inputMask;
    }

    public List<Integer> getSegmentIds() {
        return segmentIds;
    }

    public void setSegmentIds(List<Integer> segmentIds) {
        this.segmentIds = segmentIds;
    }

    public void setInputIds(List<Integer> inputIds) {
        this.inputIds = inputIds;
    }
}
