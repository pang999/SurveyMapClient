package com.surveymapclient.common;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class GLFont {
	
	/**  
     * @return ����ָ���ʺ�ָ���ַ����ĳ���  
     */  
    public static float getFontlength(Paint paint, String str) {  
        return paint.measureText(str);  
    }  
    /**  
     * @return ����ָ���ʵ����ָ߶�  
     */  
    public static float getFontHeight(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.descent - fm.ascent;    
    }   
    /**  
     * @return ����ָ���������ֶ����Ļ�׼����  
     */  
    public static float getFontLeading(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.leading- fm.ascent;    
    } 
}
