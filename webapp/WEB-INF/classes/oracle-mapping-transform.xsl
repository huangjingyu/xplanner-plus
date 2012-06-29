<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"
        doctype-public='-//Hibernate/Hibernate Mapping DTD//EN'
        doctype-system='http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd'/>

    <xsl:template match="*|@*|text()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Oracle doesn't like the = operator in an 'order by' clause -->
    <xsl:template match="//query[@name='security.role.permissions']/text()">
        <xsl:call-template name="replace-string">
            <xsl:with-param name="text" select="."/>
            <xsl:with-param name="from" select="', pr.personId = p.principalId desc'"/>
            <xsl:with-param name="to" select="''"/>
        </xsl:call-template>
    </xsl:template>

    <!-- reusable replace-string function -->
    <xsl:template name="replace-string">
        <xsl:param name="text"/>
        <xsl:param name="from"/>
        <xsl:param name="to"/>

        <xsl:choose>
            <xsl:when test="contains($text, $from)">

                <xsl:variable name="before" select="substring-before($text, $from)"/>
                <xsl:variable name="after" select="substring-after($text, $from)"/>
                <xsl:variable name="prefix" select="concat($before, $to)"/>

                <xsl:value-of select="$before"/>
                <xsl:value-of select="$to"/>
                <xsl:call-template name="replace-string">
                    <xsl:with-param name="text" select="$after"/>
                    <xsl:with-param name="from" select="$from"/>
                    <xsl:with-param name="to" select="$to"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:transform>