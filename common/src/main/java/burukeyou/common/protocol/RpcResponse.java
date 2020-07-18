package burukeyou.common.protocol;

import burukeyou.common.entity.enums.RequestTypeEnum;

public class RpcResponse extends RpcProtocol {

    /**
     *      对应的请求id
     */
    private String requestId;

    /**
     *     请求异常信息
     */
    private Exception exception;

    /**
     *   结果
     */
    private Object result;


    @Override
    public Byte getRequestType() {
        return RequestTypeEnum.RESPONSE.getType();
    }


    //
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}
