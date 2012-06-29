package com.technoetic.xplanner.export;


import java.io.ByteArrayOutputStream;

import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.classic.Session;

public class CsvExporter implements Exporter {

	private String encoding = "UTF-8";
	
	private String delimiter = ";";
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getFileExtension() {
		return "csv";
	}
	
	@Override
	public byte[] export(Session session, Object object) throws ExportException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		return data.toByteArray();
//		for (Object item : items) {
//			BeanWrapperImpl wrapper = new BeanWrapperImpl(item);
//			String[] properties = {"", ""};
//			for (String property : properties ) {
//				Object value = wrapper.getPropertyValue(property);
//				if (value instanceof Collection) {
//					Iterator<?> it = ((Collection<?>) value).iterator();
//					while (it.hasNext()) {
//						out.print(it.next());
//						out.print(' ');
//					}
//				}
//				else if (value != null) {
//					out.print(value);
//				}
//				out.print(delimiter);
//			}
//			out.println();
		}

	@Override
	public void initializeHeaders(HttpServletResponse response) {
		response.setContentType("text/csv; charset=" + encoding);
//		response.setCharacterEncoding(encoding);
	}
}