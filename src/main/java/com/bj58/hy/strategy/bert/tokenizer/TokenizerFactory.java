package com.bj58.hy.strategy.bert.tokenizer;

/**
 * Created by zhudongchang on 2021/5/28 10:45 上午
 */
public class TokenizerFactory {

    public static TokenConfig builder(){
        return new TokenConfig();
    }

    public static Tokenizer getTokenizer(TokenConfig tokenConfig){
        String path = tokenConfig.getPath();
        if(null!=path){
            Tokenizer tokenizer = new Tokenizer(tokenConfig.getPath(), tokenConfig.maxLength);
            return tokenizer;
        }

        Tokenizer tokenizer = new Tokenizer(tokenConfig.maxLength,tokenConfig.getContent());
        return tokenizer;
    }

    public static class TokenConfig {
        private String path;
        private int maxLength;
        private String content;

        public String getContent() {
            return content;
        }

        public TokenConfig setContent(String content) {
            this.content = content;
            return this;
        }

        public String getPath() {
            return path;
        }

        public TokenConfig setPath(String path) {
            this.path = path;
            return this;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public TokenConfig setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }


        public Tokenizer getTokenizer(){
            TokenConfig tokenConfig=this;
            return TokenizerFactory.getTokenizer(tokenConfig);
        }

        public static void main(String[] args) {
            String path="/Users/zhudongchang/WorkData/project/huangye_project/58huangye/huangye/branches/web/bert_tokenization_for_java/vocab.txt";
            Tokenizer tokenizer = TokenizerFactory.builder().setPath(path).setMaxLength(100).getTokenizer();
            String query = "您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？";
            TokenEntity e = tokenizer.encode(query);
            System.out.println("input_ids " + e.getInputIds());
            System.out.println("input_mask " + e.getInputMask());
            System.out.println("segment_ids " + e.getSegmentIds());


            Tokenizer tokenizerContent = TokenizerFactory.builder().setContent(FileUtils.fileGetContent(path)).setMaxLength(100).getTokenizer();
            TokenEntity encode = tokenizerContent.encode(query);
            System.out.println("input_ids " + encode.getInputIds());
            System.out.println("input_mask " + encode.getInputMask());
            System.out.println("segment_ids " + encode.getSegmentIds());
        }
    }
}

