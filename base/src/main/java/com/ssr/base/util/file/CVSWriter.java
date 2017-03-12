package com.ssr.base.util.file;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import com.ssr.base.util.Formater;
import com.ssr.base.util.StringHelper;

public class CVSWriter {
    private BufferedWriter out;

    public CVSWriter(OutputStream out) throws Exception {
        this.out = new BufferedWriter(new OutputStreamWriter(out, "GBK"));
    }

    public void newLine() throws IOException {
        this.out.write("\r\n");
    }
    
    public void writeSpliter() throws IOException {
    	this.out.write(",");
    }
    
    /**
     * 默认写入,分隔符
     * @param field
     * @throws IOException
     */
    public void write(String field) throws IOException {
    	write(field, true);
    }

    /**
     * 自行判断是否写入分隔符
     * @param field
     * @param needSpliter
     * @throws IOException
     */
    public void write(String field, boolean needSpliter) throws IOException {
        if(!StringHelper.isEmpty(field)) {
            this.out.write("\"");
            this.out.write(field);
            this.out.write("\"");
        }
        else{
        	this.out.write("\"");
            this.out.write("\"");
        }
        if(needSpliter){
        	this.out.write(",");
        }
        this.out.flush();
    }

    public void write(Object field) throws IOException {
        if(field != null) {
            this.write(field.toString());
        }

    }

    public void write(Date field) throws IOException {
        if(field != null) {
            this.write(Formater.formatDate(field));
        }

    }

    public void write(Time field) throws IOException {
        if(field != null) {
            this.write(Formater.formatTime(field));
        }

    }

    public void write(Timestamp field) throws IOException {
        if(field != null) {
            this.write(Formater.formatDateTime(field));
        }

    }

    public void write(int field) throws IOException {
        this.write(Integer.toString(field));
    }

    public void write(double field) throws IOException {
        this.write(Double.toString(field));
    }

    public void write(BigDecimal field) throws IOException {
        if(field != null) {
            this.write(Formater.formatAmount(field));
        }

    }

    public void write(float field) throws IOException {
        this.write(Float.toString(field));
    }

    public void write(short field) throws IOException {
        this.write(Short.toString(field));
    }

    public void write(byte field) throws IOException {
        this.write(Byte.toString(field));
    }

    public void write(boolean field) throws IOException {
        this.write(field?"true":"false");
    }
}
