package com.bj58.hy.strategy.bert.tokenizer;

import java.io.*;
import java.util.*;

public class Tokenizer {

    private Map<String, Integer> vocab;
    private FullTokenizer fullTokenizer;
    private Integer maxSeqLength;

    public Tokenizer(String path,int maxSeqLength){
        this.vocab = load(path);
        this.fullTokenizer = new FullTokenizer(this.vocab);
        this.maxSeqLength=maxSeqLength;
    }


    public Tokenizer(int maxSeqLength,String content){
        this.vocab = loadContent(content);
        this.fullTokenizer = new FullTokenizer(this.vocab);
        this.maxSeqLength=maxSeqLength;
    }

    private Map<String, Integer> loadContent(String content){
        Map<String, Integer> map = new HashMap<String, Integer>();

        /* 读取数据 */
        try {
            int index = 0;
            String token = null;
            String[] split = content.split("\n");
            for (String item : split) {
                map.put(item, index);
                index += 1;
            }
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        return map;
    }

    private Map<String, Integer> load(String filePath){
        Map<String, Integer> map = new HashMap<String, Integer>();

        /* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),
                    "UTF-8"));
            int index = 0;
            String token = null;
            while ((token = br.readLine()) != null) {
                map.put(token, index);
                index += 1;
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        return map;
    }

    public static void main(String[] args){

        String path="/Users/zhudongchang/WorkData/project/huangye_project/58huangye/huangye/branches/web/bert_tokenization_for_java/vocab.txt";
        Tokenizer tokenizer = new Tokenizer(path,100);
        String query = "您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？您也可以留一下邮箱的，QQ邮箱也是可以的。？";
//        String doc = "求七公主漫画1-52全集给我发一下谢谢";
//
//        List<String> docs = new ArrayList<>();
//        docs.add(doc);
//        docs.add(doc);
//
//        List<TokenEntity> examples = pp.preProcess(query, docs);
//
//        System.out.println("input_ids " + examples.get(0).getInputIds());
//        System.out.println("input_mask " + examples.get(0).getInputMask());
//        System.out.println("segment_ids " + examples.get(0).getSegmentIds());

        TokenEntity e = tokenizer.encode(query);
        System.out.println("input_ids " + e.getInputIds());
        System.out.println("input_mask " + e.getInputMask());
        System.out.println("segment_ids " + e.getSegmentIds());

    }

    // 全角转半角
    private String full2HalfChange(String QJstr){

        StringBuffer outStrBuf = new StringBuffer();

        String Tstr = "";

        byte[] b = null;
        try {
            for (int i = 0; i < QJstr.length(); i++) {

                Tstr = QJstr.substring(i, i + 1);

                if (Tstr.equals("　")) {

                    outStrBuf.append(" ");

                    continue;

                }

                b = Tstr.getBytes("unicode");

                // 得到 unicode 字节数据

                if (b[2] == -1) {

                    // 表示全角？

                    b[3] = (byte) (b[3] + 32);

                    b[2] = 0;

                    outStrBuf.append(new String(b, "unicode"));

                } else {

                    outStrBuf.append(Tstr);

                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return QJstr;
        }

        return outStrBuf.toString();
    }

    public TokenEntity encode(String query){
        List<String> tokensQuery = fullTokenizer.tokenize(query);
        return getExampleSingle(tokensQuery);
    }

    public List<TokenEntity> preProcess(String query, List<String> docs){
        String cleanQuery = full2HalfChange(query).toLowerCase();  //全角转半角+大写转小写

        List<String> tokensQuery = this.fullTokenizer.tokenize(cleanQuery);

        List<TokenEntity> examples = new ArrayList<TokenEntity>();
        for(String doc : docs){
            String cleanDoc = full2HalfChange(doc).toLowerCase();
            List<String> tokensDoc = this.fullTokenizer.tokenize(cleanDoc);
            TokenEntity e = getExamplePair(tokensQuery, tokensDoc);
            examples.add(e);
        }
        return examples;
    }

    // 句对映射id
    private TokenEntity getExamplePair(List<String> tokensQuery, List<String> tokensDoc){
        while(true){
            int totalLength = tokensQuery.size() + tokensDoc.size();
            if(totalLength <= maxSeqLength - 3){
                break;
            }
            if(tokensQuery.size() > tokensDoc.size()){
                tokensQuery.remove(tokensQuery.size() - 1);
            }else{
                tokensDoc.remove(tokensDoc.size() - 1);
            }
        }

        List<String> tokens = new ArrayList<String>();
        List<Integer> segmentIds = new ArrayList<Integer>();
        tokens.add("[CLS]");
        segmentIds.add(0);
        for(String token : tokensQuery){
            tokens.add(token);
            segmentIds.add(0);
        }
        tokens.add("[SEP]");
        segmentIds.add(0);

        for(String token : tokensDoc){
            tokens.add(token);
            segmentIds.add(1);
        }
        tokens.add("[SEP]");
        segmentIds.add(1);

        List<Integer> inputIds = this.fullTokenizer.convertTokensToIds(tokens);
        List<Integer> inputMask = new ArrayList<Integer>();

        for(int i=0; i<inputIds.size(); i++){
            inputMask.add(1);
        }

        while(inputIds.size() < maxSeqLength){
            inputIds.add(0);
            inputMask.add(0);
            segmentIds.add(0);
        }

        return new TokenEntity(inputIds, inputMask, segmentIds);

    }

    // 单句映射id
    private TokenEntity getExampleSingle(List<String> tokensQuery){
        while(true){
            int totalLength = tokensQuery.size();
            if(totalLength <= maxSeqLength - 2){
                break;
            }
            else {
                tokensQuery.remove(tokensQuery.size() - 1);
            }

        }

        List<String> tokens = new ArrayList<String>();
        List<Integer> segmentIds = new ArrayList<Integer>();
        tokens.add("[CLS]");
        segmentIds.add(0);
        for(String token : tokensQuery){
            tokens.add(token);
            segmentIds.add(0);
        }
        tokens.add("[SEP]");
        segmentIds.add(0);


        List<Integer> inputIds = this.fullTokenizer.convertTokensToIds(tokens);
        List<Integer> inputMask = new ArrayList<Integer>();

        for(int i=0; i<inputIds.size(); i++){
            inputMask.add(1);
        }

        while(inputIds.size() < maxSeqLength){
            inputIds.add(0);
            inputMask.add(0);
            segmentIds.add(0);
        }

        return new TokenEntity(inputIds, inputMask, segmentIds);

    }

}

