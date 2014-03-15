package rejasupotaro.rebuild.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
    }

    public static String capitalize(String source) {
        return Character.toUpperCase(source.charAt(0)) + source.substring(1);
    }

    public static String removeNewLines(String source) {
        return source.replaceAll("\n", "");
    }

    public static List<String> getGuestNames(String source) {
        List<String> guestNameList = new ArrayList<String>();
        if (TextUtils.isEmpty(source)) {
            return guestNameList;
        }
        if (source.indexOf("@") < 0) {
            return guestNameList;
        }

        String normarizedText = StringUtils.removeHtmlTags(source);
        String[] splitedText = normarizedText.split("@");
        for (int i = 1; i < splitedText.length; i++) {
            String guestName = getTwitterName(splitedText[i]);
            if (!TextUtils.isEmpty(guestName)) {
                guestNameList.add(guestName);
            }
        }
        return guestNameList;
    }

    public static String buildTwitterLinkText(String source) {
        if (TextUtils.isEmpty(source)) {
            return "";
        }
        if (source.indexOf("@") < 0) {
            return "";
        }

        String[] splitedSources = source.split("@");

        String twitterLinkText = "";
        twitterLinkText += splitedSources[0];
        for (int i = 1; i < splitedSources.length; i++) {
            String twitterName = getTwitterName(splitedSources[i]);
            if (TextUtils.isEmpty(twitterName)) {
                twitterLinkText += splitedSources[i];
            } else {
                twitterLinkText += appendHref("@" + twitterName, twitterName);
                twitterLinkText += splitedSources[i].substring(twitterName.length(), splitedSources[i].length());
            }
        }
        return twitterLinkText;
    }

    private static String appendHref(String text, String url) {
        return "<a href=\"https://twitter.com/" + url + "\">" + text + "</a>";
    }

    private static String getTwitterName(String source) {
        int endIndex = findEndIndex(source);
        if (endIndex < 0) {
            return "";
        }
        return source.substring(0, endIndex);
    }

    private static int findEndIndex(String source) {
        return ListUtils.min(source.indexOf(')'), source.indexOf(':'), source.indexOf(' '));
    }

    public static Spanned fromHtml(String source) {
        String replacedText = source.replaceAll("<(p|\n)*?>", "");
        return Html.fromHtml(replacedText);
    }

    public static String removeHtmlTags(String source) {
        return Html.fromHtml(source).toString();
    }
}
