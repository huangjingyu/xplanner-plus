package com.technoetic.xplanner.tags;

public class AccessKeyTransformer {
    public static String removeMnemonicMarkers(String text) {
        if (text==null) return null;
        if (getAccessKey(text) != 0) {
            return text.replaceFirst("&(.)", "<span class=\"mnemonic\">$1</span>");
        }
        return text.replaceAll("&&", "&");
    }

    public static String getHtml(String text) {
        char mnemonic = getAccessKey(text);
        if (mnemonic == 0) return "";
        String properties = " id=\"aK" + mnemonic + "\"" +
                            " accesskey=\"" + mnemonic + "\"";
        properties += " title= \"ALT+" + mnemonic +"\"";
        return properties;
    }

    public static char getAccessKey(String text) {
        if (text==null) return 0;
        int pos = text.indexOf('&');
        if (pos == -1 || pos == text.length()-1) return 0;
        char mnemonic = Character.toUpperCase(text.charAt(pos + 1));
        if (!Character.isLetterOrDigit(mnemonic)) return 0;
        return mnemonic;
    }
}