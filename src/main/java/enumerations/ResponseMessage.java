package enumerations;

public enum ResponseMessage {
    AUTH_LESS_THAN_1("权限不足：需要登陆"),
    AUTH_LESS_THAN_2("权限不足：需要管理员权限"),
    INT_PARSE_FAILED("String转换为int失败"),
    LONG_PARSE_FAILED("String转换为long失败"),
    DATE_PARSE_FAILED("String转换为Date失败"),
    DATABASE_ERROR("数据库操作失败");

    private String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return message;
    }

    public String getDetail(String... args) {
        String ins = "";
        if (args.length == 0) {
            return message;
        }
        else if (args.length == 1) {
            ins += args[0];
        }
        else {
            for (int i = 0; i < args.length; ++i) {
                if (i == args.length - 1) {
                    ins += "和" + args[i];
                } else {
                    ins += "," + args[i];
                }
            }
        }

        switch (this) {
            case INT_PARSE_FAILED:
                return ins + "不能成功转为int";
            case LONG_PARSE_FAILED:
                return ins + "不能成功转为long";
            case DATE_PARSE_FAILED:
                return ins + "不能成功转为Date";
            default:
                return message;
        }
    }
}
