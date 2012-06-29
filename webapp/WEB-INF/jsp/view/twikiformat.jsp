<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<html>
<head>
 <title>TextFormattingRules</title>
 <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
 <link rel="stylesheet" href="<html:rewrite page="/css/reset-min.css"/>" />
 <link rel="stylesheet" href="<html:rewrite page="/css/base.css"/>" />
</head>
<body bgcolor="#ffffff">
<h1><a name ="TWiki_Text_Formatting"> TWiki-style Text Formatting </a></h1>
Working with TWiki-style formatting is as easy as typing in text. You don't need
to know HTML, though you can use it if you prefer. TWiki shorthand gives you all
the power of HTML with a simple coding system that takes no time to learn. <a name="TWikiShorthand"></a>
<h2><a name ="TWiki_Editing_Shorthand"> TWiki Editing Shorthand </a></h2>
<p />
<table border="0" cellpadding="3" cellspacing="1" bgcolor="#000000">
  <tr bgcolor="#ffffff">
    <td> <strong>Formatting Command:</strong> </td>
    <td> <strong>Example: You write:</strong> </td>
    <td> <strong>You get:</strong> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Paragraphs:</strong> <br>
      Blank lines will create new paragraphs. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
1st paragraph

2nd paragraph
</pre>
      </font></span> </td>
    <td valign="top"> 1st paragraph
      <p /> 2nd paragraph
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Headings:</strong> <br>
      At least three dashes at the beginning of a line, followed by plus signs
      and the heading text. One plus creates a level 1 heading (most important),
      two pluses a level 2 heading; the maximum is level 6.
    </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
---++ Sushi

---+++ Maguro
</pre>
      </font></span> </td>
    <td valign="top">
      <h2 >Sushi</h2>
      <p />
      <h3 >Maguro</h3>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Bold Text:</strong> <br>
      Words get <strong>bold</strong> by enclosing them in <code>*</code> asterisks.
    </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
*Bold*
</pre>
      </font></span> </td>
    <td valign="top"> <strong>Bold</strong> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Italic Text:</strong> <br>
      Words get <em>italic</em> by enclosing them in <code>_</code> underscores.
    </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
_Italic_
</pre>
      </font></span> </td>
    <td valign="top"> <em>Italic</em> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Bold Italic:</strong> <br>
      Words get <em>_bold italic</em> by enclosing them in <code>_</code> double-underscores.
    </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
__Bold italic__
</pre>
      </font></span> </td>
    <td valign="top"> <strong><em>Bold italic</em></strong> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Fixed Font:</strong> <br>
      Words get shown in <code>fixed font</code> by enclosing them in <code>=</code>
      equal signs. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
=Fixed font=
</pre>
      </font></span> </td>
    <td valign="top"> <code>Fixed font</code> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Bold Fixed Font:</strong> <br>
      Words get shown in <code><b>bold fixed font</b></code> by enclosing them
      in <code><b></b></code> double equal signs. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
==Bold fixed==
</pre>
      </font></span> </td>
    <td valign="top"> <code><b>Bold fixed</b></code> </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong><em>Note:</em></strong> Make sure to "stick" the
      <code>* _ = ==</code> signs to the words, e.g. take away spaces. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
_This works_,
_this not _
</pre>
      </font></span> </td>
    <td valign="top"> <em>This works</em>, _this not _ </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Verbatim Mode:</strong> <br>
      Surround code excerpts and other formatted text with <code>&lt;verbatim&gt;</code>
      and <code>&lt;/verbatim&gt;</code> tags. <br>
      <strong><em>Note:</em></strong> Use <code>&lt;pre&gt;</code> and <code>&lt;/pre&gt;</code>
      tags instead if you want that HTML code is interpreted. <br>
      <strong><em>Note:</em></strong> Each tag must be on a line by itself. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
&lt;verbatim&gt;
class CatAnimal {
  void purr() {
    &lt;code here&gt;
  }
}
&lt;/verbatim&gt;
</pre>
      </font></span> </td>
    <td valign="top">
      <pre>
class CatAnimal {
  void purr() {
    &lt;code here&gt;
  }
}
</pre>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Separator:</strong> <br>
      At least three dashes at the beginning of a line. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
-------
</pre>
      </font></span> </td>
    <td valign="top">
      <hr />
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>List Item:</strong> <br>
      Three spaces and an asterisk. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
   * bullet item
</pre>
      </font></span> </td>
    <td valign="top">
      <ul>
        <li> bullet item
      </ul>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Nested List Item:</strong> <br>
      Six, nine, ... spaces and an asterisk. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
      * nested stuff
</pre>
      </font></span> </td>
    <td valign="top">
      <ul>
        <ul>
          <li> nested stuff
        </ul>
      </ul>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Ordered List:</strong> <br>
      Three spaces and a number. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
   1 Sushi
   1 Dim Sum
</pre>
      </font></span> </td>
    <td valign="top">
      <ol>
        <li> Sushi
        <li> Dim Sum
      </ol>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Definition List:</strong> <br>
      Three spaces, the term, a colon, a space, followed by the definition. <br>
      <strong><em>Note:</em></strong> Terms with spaces are not supported. In
      case you do have a term with more then one word, separate the words with
      dashes or with the <code>&amp;nbsp;</code> non-breaking-space entity. </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
   Sushi: Japan
   Dim&amp;nbsp;Sum: S.F.
</pre>
      </font></span> </td>
    <td valign="top">
      <dl>
        <dt> Sushi
        <dd> Japan
        <dt> Dim&nbsp;Sum
        <dd> S.F.
      </dl>
    </td>
  </tr>
  <tr bgcolor="#ffffff">
    <td valign="top"> <strong>Table:</strong> <br>
      Optional spaces followed by the cells enclosed in vertical bars. <br>
      <strong><em>Note:</em></strong> <code>| *bold* |</code> cells are rendered
      as table headers. <br>
      <strong><em>Note:</em></strong> <code>| &nbsp; spaced &nbsp; |</code> cells
      are rendered center aligned. <br>
      <strong><em>Note:</em></strong> <code>| &nbsp; &nbsp; spaced |</code> cells
      are rendered right aligned. <br>
      <strong><em>Note:</em></strong> <code>| 2 colspan ||</code> cells are rendered
      as multi-span columns. <br>
      <strong><em>Note:</em></strong> In case you have a long row and you want
      it to be more readable when you edit the table you can split the row into
      lines that end with a <code>'\'</code> backslash character. <br>
    </td>
    <td valign="top"> <span style='background : #FFFFCC;'><font color="#990000">
      <pre>
| *L* | *C* | *R* |
| A2 |  2  |  2 |
| A3 |  3  |  3 |
| multi span |||
| A4 \
  | next \
  | next |
</pre>
      </font></span> </td>
    <td valign="top">
      <table border="1" cellspacing="0" cellpadding="1">
        <tr>
          <th bgcolor="#99CCCC"> <strong>L</strong> </th>
          <th bgcolor="#99CCCC"> <strong>C</strong> </th>
          <th bgcolor="#99CCCC"> <strong>R</strong> </th>
        </tr>
        <tr>
          <td> A2 </td>
          <td align="center"> 2 </td>
          <td align="right"> 2 </td>
        </tr>
        <tr>
          <td> A3 </td>
          <td align="center"> 3 </td>
          <td align="right"> 3 </td>
        </tr>
        <tr>
          <td colspan="3"> multi span </td>
        </tr>
        <tr>
          <td> A4 </td>
          <td> next </td>
          <td> next </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<p />
<h2><a name ="Using_HTML"> </a> Using HTML </h2>
<p /> You can use just about any HTML tag without a problem - however, keep in
  mind it's preferable NOT to use HTML, and to use TWiki-style shorthand instead
  - this keeps the text uncluttered and easy to edit.
<p />
<h2><a name ="Hyperlinks"> Creating Hyperlinks </a></h2>
<p />
<ul>
<li> <code>http://...</code>, <code>https://...</code>, <code>ftp://...</code> and <code>mailto:...@...</code> are linked automatically.
</ul>
<p />
<ul>
<li> Email addresses like <code>name@domain.com</code> are linked automatically.
</ul>
<p />
<ul>
  <li> XPlanner also supports <i>custom scheme translations</i>. A URL scheme
    is the part before the first colon (e.g. http, mail, nntp, ftp, ...). If a
    custom scheme translation has been configured (see the XPlanner documentation
    for more details), it can be used to easily link to external systems. For
    example, a &quot;bug&quot; scheme might be used to link to the Bugzilla bug
    tracking system. To create a link to the bug simple type &quot;bug:000&quot;
    where 000 is replaced with the bugzilla identifier. The provide link text
    use &quot;bug:000[my link text]&quot;. (Note: this is a different style of
    external linking than TWiki uses.)
</ul>
<p />
<h2><a name ="Common_Editing_Errors"> Common Editing Errors </a></h2>
<p /> TWiki formatting rules are fairly simple to use and quick to type. However,
  there are some things to watch out for...
<p />
<ul>
<li> <strong>Q:</strong> Text enclosed in angle brackets like <code>&lt;filename&gt;</code> is not displayed. How can I show it as it is?
<ul>
<li> <strong>A:</strong> The <code>'&lt;'</code> and <code>'&gt;'</code> characters have a special meaning in HTML, they define HTML tags. You need to escape them, so write <code>'&amp;lt;'</code> instead of <code>'&lt;'</code>, and <code>'&amp;gt;'</code> instead of <code>'&gt;'</code>. <br> Example: Type <code>'prog &amp;lt;filename&amp;gt;'</code> to get <code>'prog &lt;filename&gt;'</code>.
</ul>
</ul>
<p />
<ul>
<li> <strong>Q:</strong> Why is the <code>'&amp;'</code> character sometimes not displayed?
<ul>
<li> <strong>A:</strong> The <code>'&amp;'</code> character has a special meaning in HTML, it starts a so called character entity, i.e. <code>'&amp;copy;'</code> is the <code>&copy;</code> copyright character. You need to escape <code>'&amp;'</code> to see it as it is, so write <code>'&amp;amp;'</code> instead of <code>'&amp;'</code>. <br> Example: Type <code>'This &amp;amp; that'</code> to get <code>'This &amp; that'</code>.
</ul>
</ul>
<p />
<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
<p>This help is based on the Twiki formatting instructions found at
<a target="twiki" href="http://www.twiki.org/">www.twiki.org</a>.</p>
</body>
</html>
