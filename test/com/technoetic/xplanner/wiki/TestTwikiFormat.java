package com.technoetic.xplanner.wiki;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.technoetic.xplanner.XPlannerProperties;

public class TestTwikiFormat {
   private TwikiFormat formatter;
   private HashMap schemeHandlers;

   public static class MockWikiAdapter implements ExternalWikiAdapter {
      public String formatWikiWord(String wikiWord) {
         return "(" + wikiWord + ")";
      }
   }

   @Before
   public void setUp() throws Exception {
      schemeHandlers = new HashMap();
      formatter = new TwikiFormat(schemeHandlers);
   }

   @After
   public void tearDown() throws Exception {
      if (formatter.getMalformedPatternException() != null) {
         fail(formatter.getMalformedPatternException().getMessage());
      }
      formatter.setExternalWikiAdapter(null);
      schemeHandlers.clear();
   }

   @Test
   public void testNormalTest() {
      String input = "This is\n a test.\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", input, output);
   }

   @Test
   public void testVerbatim() {
      String input = "<verbatim>\n";
      input += "   <5>\n";
      input += "</verbatim>\n";
      String expectedOutput = "<pre>\n";
      expectedOutput += "   &lt;5&gt;\n";
      expectedOutput += "</pre>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testClosingVerbatim() {
      String input = "<verbatim>\n";
      String expectedOutput = "<pre>\n</pre>";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testPreformat() {
      String input = "<pre>\n";
      input += "   <5>\n";
      input += "</pre>\n";
      String expectedOutput = "<pre>\n";
      expectedOutput += "   <5>\n";
      expectedOutput += "</pre>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testClosingPreformat() {
      String input = "<pre>\n";
      String expectedOutput = "<pre>\n</pre>";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testHtml() {
      String input = "<foo>\n";
      String expectedOutput = "<foo>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testQuoting() {
      String input = "> This is a quote.\n";
      String expectedOutput = "&gt; <cite>  This is a quote. </cite><br>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEntities() {
      String input = "&copy; & &#123;\n";
      String expectedOutput = "&copy; &amp; &#123;\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testComments() {
      String input = "<!-- this & that -->\n";
      String expectedOutput = "<!-- this &amp; that -->\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl() {
      String input = "abc http://foo.com/ def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"http://foo.com/\" target=\"_top\">http://foo.com/</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl2() {
      String input = "abc ftp://foo.com/a/b/c def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"ftp://foo.com/a/b/c\" target=\"_top\">ftp://foo.com/a/b/c</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl3() {
      String input = "abc gopher://foo.com/ def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"gopher://foo.com/\" target=\"_top\">gopher://foo.com/</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl4() {
      String input = "abc news:alt.foo def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"news:alt.foo\" target=\"_top\">news:alt.foo</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl5() {
      String input = "abc file:/usr/home/x.html def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"file:/usr/home/x.html\" target=\"_top\">file:/usr/home/x.html</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl6() {
      String input = "abc https://foo.com/ def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"https://foo.com/\" target=\"_top\">https://foo.com/</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl7() {
      String input = "xyz http://foo.com/x.gif, foo\n";

      String output = formatter.format(input);

      String expectedOutput = "xyz <img border=\"0\" src=\"http://foo.com/x.gif\"/>, foo\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl8() {
      String input = "xyz http://foo.com:8080/x.gif, foo\n";

      String output = formatter.format(input);

      String expectedOutput = "xyz <img border=\"0\" src=\"http://foo.com:8080/x.gif\"/>, foo\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl9() {
      String input = "xyz [http://foo.com:8080/x.gif], foo\n";

      String output = formatter.format(input);

      String expectedOutput = "xyz [<img border=\"0\" src=\"http://foo.com:8080/x.gif\"/>], foo\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl10() {
      String input = "xyz http://foo.com:8080/x.gif#xyz, foo\n";

      String output = formatter.format(input);

      String expectedOutput = "xyz <img border=\"0\" src=\"http://foo.com:8080/x.gif#xyz\"/>, foo\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl11() {
      String input = "xyz http://foo.com/~baz/x.html\n";

      String output = formatter.format(input);

      String expectedOutput =
            "xyz <a href=\"http://foo.com/~baz/x.html\" target=\"_top\">http://foo.com/~baz/x.html</a>\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl12() {
      String input = "xyz http://foo.com/a%20b/x.html\n";

      String output = formatter.format(input);

      String expectedOutput =
            "xyz <a href=\"http://foo.com/a%20b/x.html\" target=\"_top\">http://foo.com/a%20b/x.html</a>\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl13() {
      String input = "abc http://foo.com/a/b/c?id=fargle def\n";

      String output = formatter.format(input);

      String expectedOutput =
            "abc <a href=\"http://foo.com/a/b/c?id=fargle\" target=\"_top\">http://foo.com/a/b/c?id=fargle</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl14() {
      String url = "http://sourceforge.net/tracker/index.php?func=detail&aid=960752&group_id=49017&atid=454848";
      String input = "abc " + url + " def\n";

      String output = formatter.format(input);

      String expectedOutput = ("abc <a href=\"" + url + "\" target=\"_top\">" + url + "</a> def\n")
            .replaceAll("&", "&amp;");

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrl1WikiWord() {
      formatter.setExternalWikiAdapter(new MockWikiAdapter());
      schemeHandlers.put("wiki", new SimpleSchemeHandler("TEST$1"));
      String input = "abc wiki:WikiWord def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"TESTWikiWord\" target=\"_top\">wiki:WikiWord</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedUrlWikiWord2() {
      formatter.setExternalWikiAdapter(new MockWikiAdapter());
      schemeHandlers.put("wiki", new SimpleSchemeHandler("TEST$1"));
      String input = "abc http://foo/WikiWord def\n";

      String output = formatter.format(input);

      String expectedOutput = "abc <a href=\"http://foo/WikiWord\" target=\"_top\">http://foo/WikiWord</a> def\n";
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testExtendedUrls() {
      String input = "abc http://foo.com/[foo bar] def\n";
      String expectedOutput = "abc <a href=\"http://foo.com/\" target=\"_top\">foo bar</a> def\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testSimpleSchemeTranslation() {
      schemeHandlers.put("test", new SimpleSchemeHandler("http://test.com/test.cgi?id=$1&action=show"));
      String input = "abc test:example-100 \n";
      String expectedOutput =
            "abc <a href=\"http://test.com/test.cgi?id=example-100&action=show\" target=\"_top\">test:example-100</a> \n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testSchemeTranslation() {
      schemeHandlers.put("test", new SimpleSchemeHandler("http://test.com/test.cgi?id=$1&action=show"));
      String input = "abc test:example/x[foo bar], def\n";
      String expectedOutput =
            "abc <a href=\"http://test.com/test.cgi?id=example/x&action=show\" target=\"_top\">foo bar</a>, def\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testPropertySchemeTranslation() {
      String key = "xplanner.scr.scheme.url";
      Properties fakeProperties = new Properties(XPlannerProperties.getProperties());
      fakeProperties.setProperty(key, "http://clearquest.sabre.com?id=$1");
      formatter.setProperties(fakeProperties);
      schemeHandlers.put("cq", new PropertySimpleSchemeHandler(key));
      String input = "abc cq:123[foo bar]";
      String expectedOutput =
            "abc <a href=\"http://clearquest.sabre.com?id=123\" target=\"_new\">foo bar</a>\n";
      String output = formatter.format(input);
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testNotExistingPropertySchemeTranslation() {
      String input = "abc notExistingSchema:213435 def";
      String expectedOutput = "abc notExistingSchema:213435 def\n";
      String output = formatter.format(input);
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testSchemeTranslationNoEntry() {
      String input = "abc test:example/x[foo bar], def\n";
      String expectedOutput = "abc test:example/x[foo bar], def\n";
      String output = formatter.format(input);
      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testMailto() {
      String input = "abc mailto:jimbob@tejas.com def\n";
      String expectedOutput = "abc <a href=\"mailto:jimbob@tejas.com\">jimbob@tejas.com</a> def\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testHtmlHeadings() {
      for (int i = 1; i <= 6; i++) {
         String input = "<h" + i + ">foobar</h" + i + ">\n";
         String expectedOutput = "<nop><h" + i + "><a name=\"foobar\"> foobar </a></h" + i + ">\n";

         String output = formatter.format(input);

         assertEquals("incorrect text", expectedOutput, output);
      }
   }

   @Test
   public void testSpaceHeadings() {
      String prefix = "+";
      for (int i = 1; i <= 6; i++) {
         String input = "\t" + prefix + " foo\n";
         String expectedOutput = "<nop><h" + i + "><a name=\"foo\"> foo </a></h" + i + ">\n";

         String output = formatter.format(input);

         assertEquals("incorrect text", expectedOutput, output);
         prefix += "+";
      }

      prefix = "#";
      for (int i = 1; i <= 6; i++) {
         String input = "\t" + prefix + " foo\n";
         String expectedOutput = "<nop><h" + i + "><a name=\"foo\"> foo </a></h" + i + ">\n";

         String output = formatter.format(input);

         assertEquals("incorrect text", expectedOutput, output);
         prefix += "#";
      }
   }

   @Test
   public void testDashHeadings() {
      String prefix = "---+";
      for (int i = 1; i <= 6; i++) {
         String input = prefix + " foo\n";
         String expectedOutput = "<nop><h" + i + "><a name=\"foo\"> foo </a></h" + i + ">\n";

         String output = formatter.format(input);

         assertEquals("incorrect text", expectedOutput, output);
         prefix += "+";
      }

      prefix = "---#";
      for (int i = 1; i <= 6; i++) {
         String input = prefix + " foo\n";
         String expectedOutput = "<nop><h" + i + "><a name=\"foo\"> foo </a></h" + i + ">\n";

         String output = formatter.format(input);

         assertEquals("incorrect text", expectedOutput, output);
         prefix += "#";
      }
   }

   @Test
   public void testUnorderdAndNestedList() {
      String input = "   * item 1\n";
      input += "   * item 2\n";
      input += "      * item 3\n";
      input += "      * item 4\n";
      input += "   * item 5\n";
      input += "more text\n";
      String expectedOutput = "<ul>\n";
      expectedOutput += "<li> item 1\n";
      expectedOutput += "<li> item 2\n";
      expectedOutput += "<ul>\n";
      expectedOutput += "<li> item 3\n";
      expectedOutput += "<li> item 4\n";
      expectedOutput += "</ul>\n";
      expectedOutput += "<li> item 5\n";
      expectedOutput += "</ul>\n";
      expectedOutput += "more text\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testOrderdAndNestedList() {
      String input = "   1 item 1\n";
      input += "   1 item 2\n";
      input += "      1 item 3\n";
      input += "      1 item 4\n";
      input += "   1 item 5\n";
      input += "more text\n";
      String expectedOutput = "<ol>\n";
      expectedOutput += "<li> item 1\n";
      expectedOutput += "<li> item 2\n";
      expectedOutput += "<ol>\n";
      expectedOutput += "<li> item 3\n";
      expectedOutput += "<li> item 4\n";
      expectedOutput += "</ol>\n";
      expectedOutput += "<li> item 5\n";
      expectedOutput += "</ol>\n";
      expectedOutput += "more text\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testDefinitionList() {
      String input = "   foo: bar baz";
      String expectedOutput = "<dl>\n";
      expectedOutput += "<dt> foo<dd> bar baz\n";
      expectedOutput += "</dl>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   // Determine why *,= needs a nonspace character at end.
   public void testEmphasis() {
      String input = "__foo__\n";
      input += "*bar*\n";
      input += "==baz==\n";
      input += "_fargle_\n";
      input += "=gargle=\n";
      String expectedOutput = "<strong><em>foo</em></strong>\n";
      expectedOutput += "<strong>bar</strong>\n";
      expectedOutput += "<code><b>baz</b></code>\n";
      expectedOutput += "<em>fargle</em>\n";
      expectedOutput += "<code>gargle</code>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testHorizontalRulePlain() {
      String input = "----\n";
      String expectedOutput = "<hr/>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);

   }

   @Test
   public void testHorizontalRuleNamed() {
      String input = "Foo----\n";
      String expectedOutput = "<table width=\"100%\"><tr><td valign=\"bottom\">" +
                              "<h2>Foo</h2></td><td width=\"98%\" valign=\"middle\"><hr /></td></tr></table>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);

   }

   @Ignore
   @Test
   public void testTable() {
      String input =
            "|*abcdefg*|  ef  |  ghijk||\n" +
            "|   de  |    ab|def    |xyz|\n";
      String expectedOutput =
            "<table class=\"twiki\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\"><tr class=\"twiki\">" +
            "<th class=\"twiki\" bgcolor=\"#99CCCC\"><strong>abcdefg</strong></th><td class=\"twiki\">  ef  " +
            "</td><td colspan=\"2\" align=\"right\" class=\"twiki\">  ghijk</td></tr>\n" +
            "<tr class=\"twiki\"><td align=\"center\" class=\"twiki\">   de  </td><td align=\"right\" class=\"twiki\">    ab</td>" +
            "<td class=\"twiki\">def    </td><td class=\"twiki\">xyz</td></tr>\n</table>";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testRepeatedEmphasis() {
      String input =
            "_a_,_b_,_c_,_d_\n" +
            "*a*,*b*,*c*,*d*\n" +
            "__a__,__b__,__c__,__d__\n" +
            "=a=,=b=,=c=,=d=\n" +
            "==a==,==b==,==c==,==d==\n";
      String expectedOutput =
            "<em>a</em>,<em>b</em>,<em>c</em>,<em>d</em>\n" +
            "<strong>a</strong>,<strong>b</strong>,<strong>c</strong>,<strong>d</strong>\n" +
            "<strong><em>a</em></strong>,<strong><em>b</em></strong>,<strong><em>c</em></strong>,<strong><em>d</em></strong>\n" +
            "<code>a</code>,<code>b</code>,<code>c</code>,<code>d</code>\n" +
            "<code><b>a</b></code>,<code><b>b</b></code>,<code><b>c</b></code>,<code><b>d</b></code>\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testLinkTextWithRegexpCharacters() {
      String input = "stuff http:??? stuff\n";
      String expectedOutput = "stuff <a href=\"http:???\" target=\"_top\">http:???</a> stuff\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Ignore
   @Test
   public void testWikiWordFormattingWithDefaultFormatter() throws Exception {
      String str = formatter.format("ThisTest");
      assertEquals("<a href=\'http://c2.com/cgi/wiki?ThisTest\'>ThisTest</a>", str.trim());
   }

   @Ignore
   @Test
   public void testWikiWordFormattingEscapedWithDefaultFormatter() throws Exception {
      String str = formatter.format("!ThisTest");
      assertEquals("ThisTest", str.trim());
   }

   @Test
   public void testWikiWordFormattingWithoutFormatter() {
      formatter.setExternalWikiAdapter(null);
      String input = "stuff WikiWord stuff\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", input, output);
   }

   @Test
   public void testWikiWordFormatting() {
      formatter.setExternalWikiAdapter(new MockWikiAdapter());
      String input = "WikiWord XP.AnotherWikiWord (YetAnotherWord). OneMore\n";
      String expectedOutput = "(WikiWord) (XP.AnotherWikiWord) ((YetAnotherWord)). (OneMore)\n";

      String output = formatter.format(input);

      assertEquals("incorrect text", expectedOutput, output);
   }

   @Test
   public void testEmbeddedEmphasis() {
      String input = " a_b \n";
      String expectedOutput = " a_b \n";

      String output = formatter.format(input);
      assertEquals("incorrect text", expectedOutput, output);
   }
}
