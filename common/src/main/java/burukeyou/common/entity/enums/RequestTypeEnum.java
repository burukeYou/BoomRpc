package burukeyou.common.entity.enums;

public enum RequestTypeEnum {

    REQUEST(1),

    RESPONSE(2);

    private Byte type;

    RequestTypeEnum(int type) {
        this.type = (byte) type;
    }

    public Byte getType() {
        return type;
    }
}
