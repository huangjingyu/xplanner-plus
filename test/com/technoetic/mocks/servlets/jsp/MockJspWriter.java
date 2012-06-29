package com.technoetic.mocks.servlets.jsp;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;


public class MockJspWriter extends JspWriter {

    public StringWriter output = new StringWriter();
    public PrintWriter out = new PrintWriter(output);

    public MockJspWriter() {
        super(1, true);
    }

    public void println(char x) throws java.io.IOException {
        out.println(x);
    }

    public void println(long x) throws java.io.IOException {
        out.println(x);
    }

    public void newLine() throws java.io.IOException {
        out.println();
    }

    public void print(char[] parm1) throws java.io.IOException {
        out.print(parm1);
    }

    public void write(char[] parm1, int parm2, int parm3) throws java.io.IOException {
        out.write(parm1);
    }

    public void println(float x) throws java.io.IOException {
        out.println(x);
    }

    public void println(double x) throws java.io.IOException {
        out.println(x);
    }

    public void flush() throws java.io.IOException {
        out.flush();
    }

    public void println(Object x) throws java.io.IOException {
        out.println(x);
    }

    public void clearBuffer() throws java.io.IOException {
        //out.clearBuffer();
    }

    public int getRemaining() {
        return 0;
    }

    public void println(char[] parm1) throws java.io.IOException {
        out.println(parm1);
    }

    public void println(boolean x) throws java.io.IOException {
        out.println(x);
    }

    public void print(char c) throws java.io.IOException {
        out.print(c);
    }

    public void print(long l) throws java.io.IOException {
        out.print(l);
    }

    public void println(String x) throws java.io.IOException {
        out.println(x);
    }

    public void print(Object obj) throws java.io.IOException {
        out.print(obj);
    }

    public void close() throws java.io.IOException {
        out.close();
    }

    public void print(double d) throws java.io.IOException {
        out.print(d);
    }

    public boolean isAutoFlush() {
        return super.isAutoFlush();
    }

    public void print(boolean b) throws java.io.IOException {
        out.print(b);
    }

    public void println(int x) throws java.io.IOException {
        out.println(x);
    }

    public void print(float f) throws java.io.IOException {
        out.print(f);
    }

    public int getBufferSize() {
        return super.getBufferSize();
    }

    public void println() throws java.io.IOException {
        out.println();
    }

    public boolean printCalled;
    public String printValue;

    public void print(String value) throws java.io.IOException {
        printCalled = true;
        printValue = value;
        out.print(value);
    }

    public void clear() throws java.io.IOException {
        //output.clear();
    }

    public void print(int i) throws java.io.IOException {
        out.print(i);
    }
}