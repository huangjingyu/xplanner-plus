<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="xml"
		doctype-public='-//Hibernate/Hibernate Mapping DTD//EN'
		doctype-system='http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd' />

	<xsl:template match="*|@*|text()">
		<xsl:copy>
			<xsl:apply-templates select="*|@*|text()" />
		</xsl:copy>
	</xsl:template>

	<!--<xsl:template-->
		<!--match="//class/id/generator[@class='com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator']">-->
		<!--<generator-->
			<!--class='com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator'>-->
			<!--<xsl:element name="param"><xsl:attribute name="name">table</xsl:attribute>identifier</xsl:element>-->
			<!--<xsl:element name="param"><xsl:attribute name="name">column</xsl:attribute>nextId</xsl:element>-->
		<!--</generator>-->
	<!--</xsl:template>-->



</xsl:transform>