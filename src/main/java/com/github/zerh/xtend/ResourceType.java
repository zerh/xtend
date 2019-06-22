package com.github.zerh.xtend;

public enum ResourceType {

    ASSET_MANAGER(DefType.NULL),
    CONFIGURATION(DefType.NULL),
    NULL(DefType.NULL),

    ANIMATION(DefType.ANIM),
    ANIMATOR(DefType.ANIMATOR),
    ASSET_FILE_DESCRIPTOR(DefType.RAW),
    ATTR(DefType.ATTR),
    BOOLEAN(DefType.BOOL),
    COLOR(DefType.COLOR),
    COLOR_STATE_LIST(DefType.COLOR),
    DRAWABLE(DefType.DRAWABLE),
    ID(DefType.ID),
    INT_ARRAY(DefType.ARRAY),
    INTEGER(DefType.INTEGER),
    INTERPOLATOR(DefType.INTERPOLATOR),
    LAYOUT(DefType.LAYOUT),
    MIPMAP(DefType.MIPMAP),
    STRING(DefType.STRING),
    STRING_ARRAY(DefType.ARRAY),
    STYLE(DefType.STYLE),
    STYLEABLE(DefType.STYLEABLE),
    RAW(DefType.RAW),
    TYPEFACE(DefType.FONT),
    DIMENSION(DefType.DIMEN);

    public final String defType;

    ResourceType(final String defType) {
        this.defType = defType;
    }

    public static class DefType {
        static String NULL = "null";
        static String COLOR = "color";
        static String LAYOUT = "layout";
        static String ANIM = "anim";
        static String ANIMATOR = "animator";
        static String ATTR = "attr";
        static String ARRAY = "array";
        static String BOOL = "bool";
        static String DIMEN = "dimen";
        static String DRAWABLE = "drawable";
        static String ID = "id";
        static String INTEGER = "integer";
        static String INTERPOLATOR = "interpolator";
        static String MIPMAP = "mipmap";
        static String STRING = "string";
        static String STYLE = "style";
        static String STYLEABLE = "styleable";
        static String RAW = "raw";
        static String FONT = "font";
    }
}
