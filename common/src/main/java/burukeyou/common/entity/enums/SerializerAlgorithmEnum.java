package burukeyou.common.entity.enums;

public enum  SerializerAlgorithmEnum {

    PROTOSTUFF(1) ,

    JSON(2);

    private byte type;

    SerializerAlgorithmEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }
}
