package com.technoetic.xplanner.wiki;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.PatternMatcherInput;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * WackoWiki formatter
 *
 * www.wackowiki.com
 *
 * @author Valery Kholodkov
 */

//DEBT: Completely duplicated from TwikiFormat

public class WackoWikiFormat implements WikiFormat {
    private final Logger log = Logger.getLogger(getClass());
    private Perl5Util perl = new Perl5Util();
    private ArrayList codeStack = new ArrayList();
    private static final String mailSubstitution =
        "s/([\\s\\(])(?:mailto\\:)*([a-zA-Z0-9\\-\\_\\.\\+]+)\\@" +
        "([a-zA-Z0-9\\-\\_\\.]+)\\.([a-zA-Z0-9\\-\\_]+)(?=[\\s\\.\\,\\;\\:\\!\\?\\)])/" +
        "$1<a href=\"mailto:$2@$3.$4\">$2@$3.$4<\\/a>/go";
    private static final String fancyHr =
        "s@^([a-zA-Z0-9]+)----*@<table width=\"100%\"><tr><td valign=\"bottom\"><h2>$1</h2></td>" +
        "<td width=\"98%\" valign=\"middle\"><hr /></td></tr></table>@o";
    private static final String escapeRegexp = "s@([\\*\\?\\.\\[\\](\\)])@\\\\$1@g";
    private static final String urlPattern =
        "m@(^|[-*\\W])((\\w+):([\\w\\$\\-_\\@\\.&\\+\\?/:#%~=]+))(\\[([^\\]]+)\\]|)@";
    private static final String headerPattern = "^\\s*=(=+)([^=]+)=+\\s*$"; // '==Header=='
    private static final String wikiWordPattern = "(^|[^\\w:/])(\\w+\\.|)([A-Z][a-z]\\w*[A-Z][a-z]\\w*)(\\b|$)";
    private static final String wikiWordMatch = "m/" + wikiWordPattern + "/";
    private static Map schemeHandlers;
    private ExternalWikiAdapter externalWikiAdapter = null;
    private MalformedPerl5PatternException malformedPattern = null;
    private Properties properties = XPlannerProperties.getProperties();

   public WackoWikiFormat() {
       schemeHandlers = new HashMap();
      if (properties.getProperty("wackowiki.wikiadapter") != null) {
          try {
             externalWikiAdapter = (ExternalWikiAdapter)Class.forName(properties.
                     getProperty("wackowiki.wikiadapter")).newInstance();
          } catch (Exception e) {
              log.error("Cannot instaintiate wiki adapter", e);
              throw new RuntimeException(e);
          }
      } else {
          externalWikiAdapter = new GenericWikiAdapter();
      }
   }

    public void setSchemeHandlers(Map schemeHandlers) {
       WackoWikiFormat.schemeHandlers = schemeHandlers;
    }

    public String format(String text) {
        boolean inPreformattedSection = false;
        boolean inList = false;
        boolean inTable = false;
        PatternMatcherInput patternMatcherInput = new PatternMatcherInput("");
        BufferedReader reader = new BufferedReader(new StringReader(text));
        StringBuffer outputText = new StringBuffer();
        try {
            String line = reader.readLine();
            while (line != null) {
                try {
                    if (perl.match("m|%%|i", line)) {
                        if (!inPreformattedSection) {
                            line = perl.substitute("s|%%|<pre>|go", line);
                        } else {
                            line = perl.substitute("s|%%|</pre>|go", line);
                        }
                        inPreformattedSection = !inPreformattedSection;
                    }
                    boolean escapeBrackets = (new Boolean(
                        properties.getProperty(WikiFormat.ESCAPE_BRACKETS_KEY, "true"))).booleanValue();
                    if (inPreformattedSection) {
                        line = perl.substitute("s/&/&amp;/go", line);
                        if (escapeBrackets) {
                            line = perl.substitute("s/</&lt;/go", line);
                            line = perl.substitute("s/>/&gt;/go", line);
                        }
                        line = perl.substitute("s/&lt;pre&gt;/<pre>/go", line);
                    } else {
                        // Blockquote
                        line = perl.substitute("s|^>(.*?)$|> <cite> $1 </cite>|go", line);

                        // Embedded HTML - \263 is a special translation token
                        //    -- Allow standalone "<!--"
                        line = perl.substitute("s/<(!--)/\\\\263$1/go", line);
                        //    -- Allow standalone "-->"
                        line = perl.substitute("s/(--)>/$1\\\\263/go", line);
                        line = perl.substitute("s/<(\\S.*?)>/\\\\263$1$\\\\263/g", line);
                        if (escapeBrackets) {
                            line = perl.substitute("s/</&lt;/go", line);
                            line = perl.substitute("s/>/&gt;/go", line);
                        }
                        line = perl.substitute("s/\\\\263(\\S.*?)\\\\263/<$1>/g", line);
                        line = perl.substitute("s/(--)\\\\263/$1>/go", line);
                        line = perl.substitute("s/\\\\263(!--)/<$1/go", line);

                        // Entities
                        line = perl.substitute("s/&(\\w+?);/\\\\236$1;/g", line); // "&abc;"
                        line = perl.substitute("s/&(#[0-9]+);/\\\\236$1;/g", line); // "&#123;"
                        line = perl.substitute("s/&/&amp;/go", line); // escape standalone "&"
                        line = perl.substitute("s/\\\\236/&/go", line);

                        // Headings
                        //   ==Header== rule
                        patternMatcherInput.setInput(line);
                        while (perl.match("m|" + headerPattern + "|", patternMatcherInput)) {
                            line = perl.substitute("s@" + headerPattern + "@" +
                                                   makeAnchorHeading(perl.group(2), perl.group(1).length()) + "@goi", line);
                        }

                        // Lists etc.
                        //  Grabbed from TWiki formatter.
                        while (perl.match("m/^(\t*)   /", line)) {
                            line = perl.substitute("s/^(\t*)   /$1\t/o", line);
                        }
                        if (perl.match("m/^\\s*$/", line)) {
                            line = perl.substitute("s/^\\s*$/<p\\/>/o", line);
                            inList = false;
                        }
                        if (perl.match("m/^(\\S+?)/", line)) {
                            inList = false;
                        }
                        if (perl.match("m/^(\\t+)(\\S+?):\\s/", line)) {
                            line = perl.substitute("s/^(\\t+)(\\S+?):\\s/<dt> $2<dd> /o", line);
                            emitCode(outputText, "dl", perl.group(1).length());
                            inList = true;
                        }
                        if (perl.match("m/^(\\t+)\\* /", line)) {
                            line = perl.substitute("s/^(\\t+)\\* /<li> /o", line);
                            emitCode(outputText, "ul", perl.group(1).length());
                            inList = true;
                        }
                        if (perl.match("m/^(\\t+)\\d+\\.?/", line)) {
                            line = perl.substitute("s/^(\\t+)\\d+\\.? /<li> /o", line);
                            emitCode(outputText, "ol", perl.group(1).length());
                            inList = true;
                        }

                        if (inList == false) {
                            emitCode(outputText, "", 0);
                        }

                        // Table
                        if (perl.match("m/^(\\s*)\\|(.*)/", line)) {
                            line = perl.substitute("s/^(\\s*)\\|(.*)/" +
                                                   emitTableRow("", perl.group(2), inTable) + "/", line);
                            inTable = true;
                        } else if (inTable) {
                            outputText.append("</table>");
                            inTable = false;
                        }

                        // Italic
                        if (perl.match("m/([\\s\\(]*)\\/\\/([^\\s]+?|[^\\s].*?[^\\s])\\/\\/([\\s,.;:!?<)]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)\\/\\/([^\\s]+?|[^\\s].*?[^\\s])\\/\\/([\\s,.;:!?)<]|$)/" +
                                    "$1<i>$2<\\/i>$3/g", line);
                        }

                        // Underline
                        if (perl.match("m/([\\s\\(]*)__([^\\s]+?|[^\\s].*?[^\\s])__([\\s,.;:!?)<]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)__([^\\s]+?|[^\\s].*?[^\\s])__([\\s,.;:!?)<]|$)/" +
                                    "$1<u>$2<\\/u>$3/g", line);
                        }

                        // Monospace
                        if (perl.match("m/([\\s\\(]*)##([^\\s]+?|[^\\s].*?[^\\s])##([\\s,.;:!?)<]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)##([^\\s]+?|[^\\s].*?[^\\s])##([\\s,.;:!?)<]|$)/" +
                                    "$1<tt>$2<\\/tt>$3/g", line);
                        }

                        // Small
                        if (perl.match("m/([\\s\\(]*)\\+\\+([^\\s]+?|[^\\s].*?[^\\s])\\+\\+([\\s,.;:!?)<]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)\\+\\+([^\\s]+?|[^\\s].*?[^\\s])\\+\\+([\\s,.;:!?)<]|$)/" +
                                    "$1<small>$2<\\/small>$3/g", line);
                        }

                        // Strightthrough
                        if (perl.match("m/([\\s\\(]*)\\-\\-([^\\s]+?|[^\\s].*?[^\\s])\\-\\-([\\s,.;:!?)<]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)\\-\\-([^\\s]+?|[^\\s].*?[^\\s])\\-\\-([\\s,.;:!?)<]|$)/" +
                                    "$1<s>$2<\\/s>$3/g", line);
                        }

                        // Strong
                        if (perl.match("m/([\\s\\(]*)\\*\\*([^\\s]+?|[^\\s].*?[^\\s])\\*\\*([\\s,.;:!?)<]|$)/", line)) {
                            line = perl.substitute(
                                    "s/([\\s\\(]*)\\*\\*([^\\s]+?|[^\\s].*?[^\\s])\\*\\*([\\s,.;:!?)<]|$)/" +
                                    "$1<strong>$2<\\/strong>$3/g", line);
                        }

                        // Warning
                        if (perl.match(
                            "m/([\\s\\(]*)!!([^\\s]+?|[^\\s].*?[^\\s])!!([\\s,.;:!?)<]|$)/", line)) {
                            line =
                            perl.substitute("s/([\\s\\(]*)!!([^\\s]+?|[^\\s].*?[^\\s])!!([\\s,.;:!?)<]|$)/" +
                                            "$1<font color=\"#FF0000\">$2<\\/font>$3/g",
                                            line);
                        }

                        // Question
                        if (perl.match(
                            "m/([\\s\\(]*)\\?\\?([^\\s]+?|[^\\s].*?[^\\s])\\?\\?([\\s,.;:!?)<]|$)/",
                            line)) {
                            line =
                            perl.substitute("s/([\\s\\(]*)\\?\\?([^\\s]+?|[^\\s].*?[^\\s])\\?\\?([\\s,.;:!?)<]|$)/" +
                                            "$1<font color=\"#00FF00\">$2<\\/font>$3/g",
                                            line);
                        }

                        // Handle embedded URLs
                        patternMatcherInput.setInput(line);
                        while (perl.match(urlPattern, patternMatcherInput)) {
                            String link = perl.group(0);
                            String previousText = perl.group(1);
                            String scheme = perl.group(3);
                            String location = perl.group(4);
                            String linkText = perl.group(6);
                            String formattedLink = formatLink(previousText, scheme, location, linkText);
                            if (formattedLink != null) {
                                link = perl.substitute(escapeRegexp, link);
                                line = perl.substitute("s@" + link + "@" + formattedLink + "@go", line);
                            }
                        }

                        // Mailto
                        line = perl.substitute(mailSubstitution, line);

                        //# Horizontal rule
                        line = perl.substitute("s/^---+/<hr\\/>/o", line);
                        line = perl.substitute(fancyHr, line);

                        // WikiWord
                        if (externalWikiAdapter != null) {
                            patternMatcherInput.setInput(line);
                            while (perl.match(wikiWordMatch, patternMatcherInput)) {
                                String wikiWord = perl.group(2) + perl.group(3);
                                line = perl.substitute("s\0" +
                                                       wikiWord +
                                                       "\0" +
                                                       /*perl.group(1)+*/
                                                       externalWikiAdapter.formatWikiWord(wikiWord) +
                                                       perl.group(4) +
                                                       "\0", line);
                            }
                        }
                    }
                } catch (MalformedPerl5PatternException ex) {
                    // just continue, set flag for testing purposes
                    malformedPattern = ex;
                }
                outputText.append(line);
                outputText.append("<br>");
                line = reader.readLine();
            }
            emitCode(outputText, "", 0);
            if (inTable) {
                outputText.append("</table>");
            }
            if (inPreformattedSection) {
                outputText.append("</pre>");
            }
        } catch (Exception ex) {
            log.error("error during formatting", ex);
            outputText.setLength(0);
            outputText.append("[Error during formatting]");
        }
        return outputText.toString();
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    private String formatLink(String previousText,
                              String scheme,
                              String location,
                              String linkText) {
        if (scheme.equals("mailto")) {
            return null;
        }

        SchemeHandler handler = (SchemeHandler) schemeHandlers.get(scheme);
        if (handler != null) {
            return previousText + handler.translate(properties, scheme, location, linkText);
        }
        String url = scheme + ":" + location;
        if (perl.match("m/http|ftp|gopher|news|file|https/", scheme)) {
            if (linkText == null) {
                linkText = url;
            }
            if (perl.match("m|\\.(gif|jpg|jpeg|png)(#|$)|i", url)) {
                return previousText + "<img border=\"0\" src=\"" + url + "\"/>";
            } else {
                return previousText + "<a href=\"" + url + "\" target=\"_top\">" + linkText + "</a>";
            }
        }
        return previousText + url + "[" + linkText + "]";
    }

    private String makeAnchorName(String text) {
        text = perl.substitute("s/^[\\s\\#\\_]*//o", text);      //no leading space nor '#', '_'
        text = perl.substitute("s/[\\s\\_]*$//o", text);        // no trailing space, nor '_'
        text = perl.substitute("s/<\\w[^>]*>//goi", text);     //remove HTML tags
        text = perl.substitute("s/[^a-zA-Z0-9]/_/go", text);  // only allowed chars
        text = perl.substitute("s/__+/_/go", text);           // remove excessive '_'
        text = perl.substitute("s/^(.{32})(.*)$/$1/o", text); // limit to 32 chars
        return text;
    }

    private String makeAnchorHeading(String text, int level) {
        // - Need to build '<nop><h1><a name="atext"> text </a></h1>'
        //   type markup.
        // - Initial '<nop>' is needed to prevent subsequent matches.
        // - Need to make sure that <a> tags are not nested, i.e. in
        //   case heading has a WikiName that gets linked
        String anchorName = makeAnchorName(text);
        boolean hasAnchor =
            perl.match("m/<a /i", text) ||
            perl.match("m/\\[\\[/", text) ||
            perl.match("m/(^|[\\*\\s][\\-\\*\\s]*)([A-Z]{3,})/", text) ||
            perl.match("m/(^|[\\*\\s][\\(\\-\\*\\s]*)([A-Z]+[a-z0-9]*)\\.([A-Z]+[a-z]+[A-Z]+[a-zA-Z0-9]*)/", text) ||
            perl.match("m/(^|[\\*\\s][\\(\\-\\*\\s]*)([A-Z]+[a-z]+[A-Z]+[a-zA-Z0-9]*)/", text);
        if (hasAnchor) {
            text = "<nop><h" + level + "><a name=\"" + anchorName + "\"> </a> " + text + "</h" + level + ">";
        } else {
            text = "<nop><h" + level + "><a name=\"" + anchorName + "\"> " + text + " </a></h" + level + ">";
        }
        return text;
    }

    public void emitCode(StringBuffer result, String code, int depth) {
        while (codeStack.size() > depth) {
            String c = (String) codeStack.remove(codeStack.size() - 1);
            result.append("</").append(c).append(">\n");
        }
        while (codeStack.size() < depth) {
            codeStack.add(code);
            result.append("<").append(code).append(">\n");
        }

//if( ( $#code > -1 ) && ( $code[$#code] ne $code ) ) {
        if (!codeStack.isEmpty() && !codeStack.get(codeStack.size() - 1).equals(code)) {
            result.append("</").append(codeStack.get(codeStack.size() - 1)).
                append("><").append(code).append(">\n");
            codeStack.set(codeStack.size() - 1, code);
        }
    }

    public String emitTableRow(String previousText, String row, boolean inTable) {
        StringBuffer result = new StringBuffer();
        if (inTable) {
            result.append(previousText).append("<tr class=\"twiki\">");
        } else {
            result.append(previousText);
            result.append(
                "<table class=\"twiki\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\">");
            result.append("<tr class=\"twiki\">");
        }
        row = perl.substitute("s/\\t/    /go", row); // change tab to spaces
        row = perl.substitute("s/\\s*$//o", row);  // remove trailing white space
        while (perl.match("m/(\\|\\|+)/", row)) {
            // calc COLSPAN
            row = perl.substitute("s/(\\|\\|+)/\\\\236" + perl.group(1).length() + "\\|/go", row);
        }

        ArrayList cells = new ArrayList();
        perl.split(cells, "/\\|/", row);
        for (int i = 0, n = cells.size() - 1; i < n; i++) {
            String cell = (String) cells.get(i);
            String attribute = "";
            if (perl.match("m/\\\\236([0-9]+)/", cell)) {
                cell = perl.substitute("s/\\\\236([0-9]+)//", cell);
                attribute = " colspan=\"" + Integer.parseInt(perl.group(1)) + "\"";
            }
            cell = perl.substitute("s/^\\s+$/ &nbsp; /o", cell);
            perl.match("m/^(\\s*).*?(\\s*)$/", cell);
            String left = perl.group(1);
            String right = perl.group(2);
            if (left.length() > right.length()) {
                if (right.length() <= 1) {
                    attribute += " align=\"right\"";
                } else {
                    attribute += " align=\"center\"";
                }
            }
            if (perl.match("m/^\\s*(\\*.*\\*)\\s*$/", cell)) {
                result.append("<th").append(attribute).append(" class=\"twiki\" bgcolor=\"#99CCCC\">").
                    append(perl.group(1)).append("<\\/th>");
            } else {
                result.append("<td").append(attribute).append(" class=\"twiki\">").
                    append(cell).append("<\\/td>");
            }
        }
        result.append("<\\/tr>");
        return result.toString();
    }

    public void setExternalWikiAdapter(ExternalWikiAdapter wikiWordFormatter) {
        this.externalWikiAdapter = wikiWordFormatter;
    }

    public MalformedPerl5PatternException getMalformedPatternException() {
        return malformedPattern;
    }
}
