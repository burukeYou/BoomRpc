package burukeyou.common.protocol;


import burukeyou.common.entity.enums.RequestTypeEnum;

import java.util.Arrays;

public class RpcRequest extends RpcProtocol  {

    /**
     *      请求id
     */
    private String requestId;

    /**
     *  调用类名
     */
    private String className;

    /**
     *  调用方法名
     */
    private String methodName;

    /**
     *  方法参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     *  方法参数
     */
    private Object[] parameters;


    @Override
    public Byte getRequestType() {
        return RequestTypeEnum.REQUEST.getType();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }


    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
