/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

/**
 *
 * @author Dangdang.Cao
 */
import com.gome.test.gtp.model.CaseResult;
import com.gome.test.gtp.model.ExcelModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AgentInfoExcelDownLoad extends BaseExcelDownLoad {
    @Override
    public ExcelModel createDownLoadExcel(List list, ExcelModel excel) throws Exception {
        
        String titleStr = "reportID;TestCase;IsRerun;Computer;Duration;Result;ErrorMessage;StackTrace;FailReason;";
    
        ArrayList data = new ArrayList();        
        
        Iterator ir = list.iterator();
        while(ir.hasNext()){
            
            ArrayList rowData = new ArrayList();
            
            CaseResult result = (CaseResult)ir.next();
        
//            rowData.add(result.getReportId());
            rowData.add(result.getTestCaseName());            
//            rowData.add(result.isReRun());
            rowData.add(result.getComputerName());
            rowData.add(result.getDuration());
            rowData.add(result.getTestResult());
            
            rowData.add(result.getErrorMessage()==null?"":result.getErrorMessage());
            rowData.add(result.getStackTrace()==null?"":result.getErrorMessage());
            rowData.add(result.getFailReason()==null?"":result.getErrorMessage());
            data.add(rowData);        
        }
        String[] titles = titleStr.split(";");

        ArrayList header = new ArrayList();
        for (int i=0;i<titles.length;i++){
            header.add(titles[i]);
        }
        
        //设置报表标题
        excel.setHeader(header);
        //设置报表内容
        excel.setData(data);
        return excel;
    }
    
       
}

