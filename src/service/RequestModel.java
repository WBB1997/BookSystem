package service;

import java.io.Serializable;
import java.util.List;

/**
 * 显示数据的实体类
 * @Author: fangju
 * @Date: 2019/6/24
 */
public class RequestModel<M> implements Serializable {
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    private Integer code;
    private String msg;
    private Integer count;
    private List<M> data;

    private RequestModel(Integer code, String msg, Integer count, List<M> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<M> getData() {
        return data;
    }

    public void setData(List<M> data) {
        this.data = data;
    }

    public static <M> RequestModel<M> buildError(){
        return new RequestModel<>(ERROR,"获取失败",0,null);
    }

    public static <M> RequestModel<M> buildSuccess(Integer count,List<M> data){
        return new RequestModel<>(SUCCESS,"ok",count,data);
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                ", \"count\":" + count +
                ", \"data\":" + data +
                '}';
    }
}
