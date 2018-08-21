/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import com.gome.test.gtp.model.ExcelModel;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dangdang.Cao
 */
public interface ExcelDownLoad {

    /**
     * 初始化要生成的Excel的表模型
     *
     * @param list List　填充了 Excel表格数据的集合
     * @param form ActionForm及其子类
     * @param excel ExcelModel Excel表的对象模型
     * @return 
     * @see ExcelModel
     * @throws Exception
     */
    public ExcelModel createDownLoadExcel(List list, ExcelModel excel) throws Exception;

    /**
     * 在已文件已存在的情况下，采用读取文件流的方式实现左键点击下载功能， 本系统没有采取这个方法,而是直接将数据传往输出流,效率更高。
     *
     * @param inPutFileName 读出的文件名
     * @param outPutFileName　保存的文件名
     * @param response

* @see HttpServletResponse
     * @throws IOException
     */
    public void downLoad(String inPutFileName, String outPutFileName,
            HttpServletResponse response) throws IOException;

    /**
     * 在已文件不存在的情况下，采用生成输出流的方式实现左键点击下载功能。
     *
     * @param outPutFileName　保存的文件名
     * @param downExcel 填充了数据的ExcelModel
     * @param response
     * @see HttpServletResponse
     * @throws Exception
     */
    public void downLoad(String outPutFileName, ExcelModel downExcel,
            HttpServletResponse response) throws Exception;

}

